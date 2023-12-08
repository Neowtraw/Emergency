package com.codingub.emergency.data.remote.datasource

import com.codingub.emergency.common.ResultState
import com.codingub.emergency.data.remote.responses.ArticleResponse
import com.codingub.emergency.domain.models.Article
import kotlinx.coroutines.flow.Flow


interface FireDataSource {
    suspend fun getArticles() : List<Article>
    suspend fun searchArticles(alt: String) : List<Article>
}
