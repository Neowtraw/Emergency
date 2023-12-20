package com.codingub.emergency.di

import com.codingub.emergency.data.remote.datasource.VolleyDataSource
import com.codingub.emergency.data.remote.datasource.VolleyDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    abstract fun bindVolleyDataSource(volleyDataSource: VolleyDataSourceImpl) : VolleyDataSource
}