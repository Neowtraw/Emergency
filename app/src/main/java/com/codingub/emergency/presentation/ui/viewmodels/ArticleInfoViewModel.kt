package com.codingub.emergency.presentation.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.codingub.emergency.presentation.ui.screens.video.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArticleInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val player: Player
) : ViewModel() {

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

    fun setVideoUri(uri: String) {
        savedStateHandle["videoUri"] = uri
        player.addMediaItem(MediaItem.fromUri(uri))
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

}