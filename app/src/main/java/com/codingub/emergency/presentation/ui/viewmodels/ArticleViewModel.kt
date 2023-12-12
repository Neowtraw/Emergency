package com.codingub.emergency.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingub.emergency.common.ResultState
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.use_cases.GetArticles
import com.codingub.emergency.domain.use_cases.SearchArticles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val getArticles: GetArticles,
    private val searchArticles: SearchArticles
) : ViewModel(){

    private val _articles: MutableStateFlow<ResultState<List<Article>>> = MutableStateFlow(ResultState.Loading())
    val articles: StateFlow<ResultState<List<Article>>> = _articles.asStateFlow()

    init {
        getAllArticles()
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
        viewModelScope.launch(Dispatchers.IO) {
            val articlesFlow = searchArticles(alt)
            articlesFlow.collect { arts ->
                _articles.value = arts
            }
        }
    }
}