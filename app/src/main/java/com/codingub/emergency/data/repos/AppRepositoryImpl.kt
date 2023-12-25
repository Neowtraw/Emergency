package com.codingub.emergency.data.repos

import com.codingub.emergency.core.ResultState
import com.codingub.emergency.data.local.datasource.LocalDataSource
import com.codingub.emergency.data.remote.datasource.FireDataSource
import com.codingub.emergency.data.utils.network.ConnectivityObserver
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.models.Service
import com.codingub.emergency.domain.repos.AppRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val fireDataSource: FireDataSource,
    private val localDataSource: LocalDataSource,
    private val network: ConnectivityObserver
) : AppRepository {

    override suspend fun updateFavoriteArticle(id: String, liked: Boolean) =
        localDataSource.updateFavoriteArticle(id, liked)

    override fun getArticles(): Flow<ResultState<List<Article>>> = channelFlow {
        try {
            network.observe().collectLatest { status ->
                val result: ResultState<List<Article>> = when (status) {
                    ConnectivityObserver.Status.Available -> {
                        val data = fireDataSource.getArticles().first()

                        if (data is ResultState.Success) {
                            withContext(NonCancellable) {
                                localDataSource.insertArticles(data.data!!)
                            }
                           ResultState.Success(localDataSource.getAllArticles().first())
                        } else {
                            data
                        }
                    }
                    ConnectivityObserver.Status.Losing -> {
                        if (localDataSource.getAllArticles().first().isEmpty()) {
                            val data = fireDataSource.getArticles().first()

                            if (data is ResultState.Success) {
                                withContext(NonCancellable) {
                                    // localDataSource.insertArticles(data.data!!)
                                }
                            }
                            data
                        } else {
                            ResultState.Success(localDataSource.getAllArticles().first())
                        }
                    }

                    else -> ResultState.Success(localDataSource.getAllArticles().first())
                }
                send(result)
            }
            send(ResultState.Success(localDataSource.getAllArticles().first()))
        } catch (e: Throwable) {
            e.printStackTrace()
            send(ResultState.Error(e))
        }
    }

    override fun getArticle(id: String): Flow<Article> = localDataSource.getArticle(id)
    override fun getFavoriteArticles(): Flow<List<Article>> = localDataSource.getFavoriteArticles()
    override fun searchArticles(alt: String): Flow<List<Article>> =
        localDataSource.searchArticles(alt)


    override fun getServicesFromLanguage(language: String): Flow<ResultState<List<Service>>> =
        channelFlow {
            try {
                network.observe().collectLatest { status ->
                    val result: ResultState<List<Service>> = when (status) {
                        ConnectivityObserver.Status.Available -> {
                            val data = fireDataSource.getServicesFromLanguage(language).first()

                            if (data is ResultState.Success) {
                                withContext(NonCancellable) {
                                    //   localDataSource.insertServices(data.data!!)
                                }
                            }
                            data
                        }

                        ConnectivityObserver.Status.Losing -> {
                            if (localDataSource.getSavedServices().first().isEmpty()) {
                                val data = fireDataSource.getServicesFromLanguage(language).first()

                                if (data is ResultState.Success) {
                                    withContext(NonCancellable) {
                                        // localDataSource.insertArticles(data.data!!)
                                    }
                                }
                                data
                            } else {
                                ResultState.Success(localDataSource.getSavedServices().first())
                            }
                        }

                        else -> {
                            ResultState.Success(localDataSource.getSavedServices().first())
                        }
                    }
                    send(result)
                }
            } catch (e: Throwable) {
                send(ResultState.Error(e))
            }
        }
}