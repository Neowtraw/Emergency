package com.codingub.emergency.data.utils.network

import kotlinx.coroutines.flow.Flow


interface ConnectivityObserver {

    fun observe() : Flow<Status>

    enum class Status{
        Available, Unavailable, Losing, Lost
    }
}