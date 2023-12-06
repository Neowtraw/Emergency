package com.codingub.emergency.data.remote.datasource

import com.codingub.emergency.common.ResultState
import com.codingub.emergency.data.remote.responses.ArticleResponse
import com.codingub.emergency.domain.models.Article
import kotlinx.coroutines.flow.Flow


interface FireDataSource {
    fun getArticles() : Flow<ResultState<List<Article>>>
    fun searchArticles(alt: String) : Flow<ResultState<List<Article>>>
}
