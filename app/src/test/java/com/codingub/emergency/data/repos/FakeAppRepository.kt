package com.codingub.emergency.data.repos

import com.codingub.emergency.core.ResultState
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.models.Service
import com.codingub.emergency.domain.repos.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAppRepository : AppRepository {

    private val articles = mutableListOf<Article>()
    private val services = mutableListOf<Service>()

    override suspend fun updateFavoriteArticle(id: String, liked: Boolean) {
       articles.find { it.id == id }?.let{ it.liked = liked }
    }

    override fun getArticles(): Flow<ResultState<List<Article>>> = flow {
        emit(ResultState.Loading())
        emit(ResultState.Success(articles))
    }

    override fun getArticle(id: String): Flow<Article>  = flow {
       emit(articles.find { it.id == id } ?: Article())
    }

    override fun getFavoriteArticles(): Flow<List<Article>> = flow {
        emit(articles.filter { it.liked })
    }

    override fun searchArticles(alt: String): Flow<ResultState<List<Article>>> = flow {
        emit(ResultState.Loading())
        emit(ResultState.Success(articles))
    }

    override fun getServicesFromLanguage(language: String): Flow<ResultState<List<Service>>> = flow {
        emit(ResultState.Success(services.filter { it.language == language }))
    }
}