package com.codingub.emergency.presentation.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.use_cases.GetArticle
import com.codingub.emergency.domain.use_cases.UpdateFavoriteArticle
import com.codingub.emergency.presentation.ui.screens.video.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val player: Player,
    private val updateFavoriteArticle: UpdateFavoriteArticle,
    private val getArticle: GetArticle
) : ViewModel() {

    private val _article = MutableStateFlow<Article>(Article())
    val article = _article.asStateFlow()

    private val videoUri = savedStateHandle.getStateFlow("videoUri", "")
    val videoItem: StateFlow<VideoItem> = videoUri.map { uri ->
        VideoItem(
            uri = Uri.parse(uri),
            mediaItem = MediaItem.fromUri(Uri.parse(uri))
        )
    }.stateIn(
        viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = VideoItem(
            uri = Uri.parse(""),
            mediaItem = MediaItem.fromUri(Uri.parse(""))
        )
    )

    init {
        player.prepare()
    }

    fun getSavedArticle(id: String) {
        viewModelScope.launch {
            getArticle(id).collectLatest {
                _article.value = it
                setVideoUri()
            }
        }
    }

    private fun setVideoUri() {
        savedStateHandle["videoUri"] = _article.value.videoUrl
        player.addMediaItem(MediaItem.fromUri(_article.value.videoUrl!!))
    }

    fun playVideo(uri: Uri) {
        player.setMediaItem(
            videoItem.value.mediaItem
        )
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

    fun updateArticleToFavorite(id: String, liked: Boolean) {
        viewModelScope.launch {
            updateFavoriteArticle(id, liked)
        }
    }

}