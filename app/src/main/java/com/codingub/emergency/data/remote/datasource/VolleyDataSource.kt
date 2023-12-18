package com.codingub.emergency.data.remote.datasource

import com.codingub.emergency.data.remote.models.NewsDao

interface VolleyDataSource {

    suspend fun getTheLastNewsLink() : String
    suspend fun getTheLastNews(href: String) : NewsDao
}