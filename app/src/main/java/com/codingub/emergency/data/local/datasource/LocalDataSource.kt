package com.codingub.emergency.data.local.datasource

import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.models.Service
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    /*
        Articles
     */
    suspend fun insertArticles(articles: List<Article>)
    suspend fun updateFavoriteArticle(id: String, liked: Boolean)

    fun getFavoriteArticles() : Flow<List<Article>>
    fun getAllArticles() : Flow<List<Article>>
    fun getArticle(id: String) : Flow<Article>
    fun searchArticles(alt: String) : Flow<List<Article>>

    /*
        Services
     */
    suspend fun insertServices(services: List<Service>)
    fun getSavedServices() : Flow<List<Service>>
}