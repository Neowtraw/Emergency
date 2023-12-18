package com.codingub.emergency.data.remote.datasource

import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.models.Service


interface FireDataSource {
    /*
        Articles
     */
    suspend fun getArticles() : List<Article>
    suspend fun searchArticles(alt: String) : List<Article>
    /*
        Services
     */
    suspend fun getServicesFromLanguage(language: String) : List<Service>
}
