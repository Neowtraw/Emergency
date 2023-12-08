package com.codingub.emergency.di

import com.codingub.emergency.data.local.datasource.LocalDataSource
import com.codingub.emergency.data.local.datasource.LocalDataSourceImpl
import com.codingub.emergency.data.remote.datasource.FireDataSource
import com.codingub.emergency.data.remote.datasource.FireDataSourceImpl
import com.codingub.emergency.data.repos.AppRepositoryImpl
import com.codingub.emergency.domain.repos.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindLocalDataSource(localDataSource: LocalDataSourceImpl): LocalDataSource

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSource: FireDataSourceImpl): FireDataSource

    @Binds
    abstract fun bindRepository(repository: AppRepositoryImpl): AppRepository
}