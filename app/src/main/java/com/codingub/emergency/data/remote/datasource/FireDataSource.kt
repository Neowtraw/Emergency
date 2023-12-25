package com.codingub.emergency.data.remote.datasource

import com.codingub.emergency.core.ResultState
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.models.Service
import kotlinx.coroutines.flow.Flow


interface FireDataSource {

    fun getArticles(): Flow<ResultState<List<Article>>>
    fun getServicesFromLanguage(language: String): Flow<ResultState<List<Service>>>
}
