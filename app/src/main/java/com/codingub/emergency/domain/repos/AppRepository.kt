package com.codingub.emergency.domain.repos

import com.codingub.emergency.common.ResultState
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.models.Service
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    /*
        Articles
     */
    suspend fun updateFavoriteArticle(article: Article)
    fun getArticles() : Flow<ResultState<List<Article>>>
    fun getArticle(id: String) : Flow<Article>
    fun getFavoriteArticles() : Flow<List<Article>>
    fun searchArticles(alt: String) : Flow<ResultState<List<Article>>>
    suspend fun isFavoriteArticle(id: String) : Boolean

    /*
        Services
     */
    fun getServicesFromLanguage(language: String) :  Flow<ResultState<List<Service>>>
}
