package com.codingub.emergency.presentation.ui.viewmodels

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingub.emergency.core.Resource
import com.codingub.emergency.core.ResultState
import com.codingub.emergency.data.utils.NetworkLostException
import com.codingub.emergency.data.utils.NoResultsException
import com.codingub.emergency.data.utils.UnknownErrorException
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.use_cases.GetArticles
import com.codingub.emergency.domain.use_cases.SearchArticles
import com.codingub.emergency.domain.use_cases.UpdateFavoriteArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val getArticles: GetArticles,
    private val searchArticles: SearchArticles,
    private val updateFavoriteArticle: UpdateFavoriteArticle,
    private val resources: Resource
) : ViewModel() {

    private val _articles: MutableStateFlow<ResultState<List<Article>>> =
        MutableStateFlow(ResultState.Loading())
    val articles: StateFlow<ResultState<List<Article>>> = _articles.asStateFlow()

    val articlesState: StateFlow<ArticlesState> = articles.map { result ->
        Log.d("test", result.toString())

        when (result) {

            is ResultState.Success -> {
                if (result.data.isNullOrEmpty()) ArticlesState.NotFound(NoResultsException(resources).message)
                else ArticlesState.Success(result.data)
            }
            is ResultState.Loading -> ArticlesState.Loading
            is ResultState.Error -> {
                if (result.error is NetworkErrorException ||
                    result.error is UnknownHostException
                ) ArticlesState.NetworkLost(
                    NetworkLostException(resources).message
                )
                else ArticlesState.Error(
                    result.error?.message ?: UnknownErrorException(resources).message
                )
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        ArticlesState.Loading
    )


    init {
        getAllArticles()
    }

    fun updateArticleToFavorite(id: String, liked: Boolean) {
        viewModelScope.launch {
            updateFavoriteArticle(id, liked)
        }
    }

    private fun getAllArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            val articlesFlow = getArticles()
            articlesFlow.collect { arts ->
                _articles.value = arts
            }
        }
    }

    fun searchArticlesByAlt(alt: String) {
        _articles.value = ResultState.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val articlesFlow = searchArticles(alt)
            articlesFlow.collect { arts ->
                _articles.value = ResultState.Success(arts)
            }
        }
    }
}

sealed class ArticlesState {
    object Loading : ArticlesState()
    data class NotFound(val error: String) : ArticlesState()
    data class NetworkLost(val error: String) : ArticlesState()
    data class Success(val articles: List<Article>) : ArticlesState()
    data class Error(val error: String) : ArticlesState()
}