package com.codingub.emergency.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.use_cases.GetFavoriteArticles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFavoriteArticles: GetFavoriteArticles
) : ViewModel() {

    private val _articles: MutableStateFlow<List<Article>> = MutableStateFlow(emptyList())
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()


    init {
        getArticles()
    }

    private fun getArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            getFavoriteArticles().collect { arts ->
                _articles.value = arts
            }
        }
    }
}