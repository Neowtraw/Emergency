package com.codingub.emergency.data.remote.datasource

import com.codingub.emergency.domain.models.News

interface VolleyDataSource {

    suspend fun getTheLastNewsLink() : String
    suspend fun getTheLastNews(href: String) : News
}