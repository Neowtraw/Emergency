package com.codingub.emergency.data.repos

import com.codingub.emergency.common.ResultState
import com.codingub.emergency.data.local.datasource.LocalDataSource
import com.codingub.emergency.data.remote.datasource.FireDataSource
import com.codingub.emergency.data.utils.NetworkBoundResultState
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.models.Service
import com.codingub.emergency.domain.repos.AppRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val fireDataSource: FireDataSource,
    private val localDataSource: LocalDataSource,
    private val dispatcher: CoroutineDispatcher
) : AppRepository {

    override suspend fun updateFavoriteArticle(article: Article) =
        localDataSource.updateFavoriteArticle(article)

    override fun getArticles(): Flow<ResultState<List<Article>>> = NetworkBoundResultState(
        query = {
            localDataSource.getAllArticles()
        },
        shouldFetch = {
            it.isNullOrEmpty()
        },
        fetch = {
            fireDataSource.getArticles()
        },
        saveFetchResult = {
            localDataSource.insertArticles(it)
        },
        onFetchFailed = {

        },
        dispatcher = dispatcher
    )


    override fun getArticle(id: String): Flow<Article> = localDataSource.getArticle(id)

    override fun getFavoriteArticles(): Flow<List<Article>> = localDataSource.getFavoriteArticles()

    override fun searchArticles(alt: String): Flow<ResultState<List<Article>>> = callbackFlow {
        trySend(ResultState.Loading())
        try {
            val data = fireDataSource.searchArticles(alt)
            trySend(ResultState.Success(data))
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            trySend(ResultState.Error(e))

        }
    }

    override suspend fun isFavoriteArticle(id: String): Boolean =
        localDataSource.isFavoriteArticle(id)

    override fun getServicesFromLanguage(language: String): Flow<ResultState<List<Service>>> =
        NetworkBoundResultState(
            query = {
                localDataSource.getSavedServices()
            },
            shouldFetch = {
                it.isNullOrEmpty() || it.none { service -> service.language == language }
            },
            fetch = {
                fireDataSource.getServicesFromLanguage(language = language)
            },
            saveFetchResult = {
                localDataSource.insertServices(services = it)
            },
            onFetchFailed = {

            },
            dispatcher = dispatcher
        )
}