package com.codingub.emergency.di

import com.codingub.emergency.data.local.datasource.LocalDataSource
import com.codingub.emergency.data.local.datasource.LocalDataSourceImpl
import com.codingub.emergency.data.remote.datasource.FireDataSource
import com.codingub.emergency.data.remote.datasource.FireDataSourceImpl
import com.codingub.emergency.data.repos.AppRepositoryImpl
import com.codingub.emergency.data.repos.AuthRepositoryImpl
import com.codingub.emergency.data.utils.network.ConnectivityObserver
import com.codingub.emergency.data.utils.network.NetworkManager
import com.codingub.emergency.domain.repos.AppRepository
import com.codingub.emergency.domain.repos.AuthRepository
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
    abstract fun bindNetworkManager(network: NetworkManager): ConnectivityObserver

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSource: FireDataSourceImpl): FireDataSource

    @Binds
    abstract fun bindRepository(repository: AppRepositoryImpl): AppRepository

    @Binds
    abstract fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository
}