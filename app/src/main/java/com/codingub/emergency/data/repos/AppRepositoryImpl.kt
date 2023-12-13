package com.codingub.emergency.data.repos

import android.util.Log
import com.codingub.emergency.common.ResultState
import com.codingub.emergency.data.local.datasource.LocalDataSource
import com.codingub.emergency.data.remote.datasource.FireDataSource
import com.codingub.emergency.data.utils.NetworkBoundResultState
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.models.Service
import com.codingub.emergency.domain.repos.AppRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val fireDataSource: FireDataSource,
    private val localDataSource: LocalDataSource,
    private val dispatcher: CoroutineDispatcher
) : AppRepository {

    override suspend fun updateFavoriteArticle(id: String, liked: Boolean) =
        localDataSource.updateFavoriteArticle(id, liked)

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
            Log.d("searchArticles", alt)

            val data = fireDataSource.searchArticles(alt)
            trySend(ResultState.Success(data))
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            send(ResultState.Error(e))
            Log.d("searchArticles", "error")
        }

        awaitClose { cancel() }
    }


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