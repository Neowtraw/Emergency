package com.codingub.emergency.data.local.datasource

import com.codingub.emergency.domain.models.Article
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun insertArticles(articles: List<Article>)
    suspend fun updateFavoriteArticle(article: Article)

    fun getFavoriteArticles() : Flow<List<Article>>
    fun getAllArticles() : Flow<List<Article>>

    fun getArticle(id: String) : Flow<Article>
    suspend fun isFavoriteArticle(id: String) : Boolean
}