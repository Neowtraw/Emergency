package com.codingub.emergency.domain.repos

import com.codingub.emergency.common.ResultState
import com.codingub.emergency.domain.models.Article
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun updateFavoriteArticle(article: Article)
    fun getArticles() : Flow<ResultState<List<Article>>>
    fun getArticle(id: String) : Flow<Article>
    fun getFavoriteArticles() : Flow<List<Article>>
    fun searchArticles(alt: String) : Flow<ResultState<List<Article>>>
    suspend fun isFavoriteArticle(id: String) : Boolean
}
