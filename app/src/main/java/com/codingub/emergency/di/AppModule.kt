package com.codingub.emergency.di

import android.content.Context
import com.codingub.emergency.data.local.datasource.LocalDataSource
import com.codingub.emergency.data.repos.AppRepositoryImpl
import com.codingub.emergency.data.repos.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext context: Context
    ) = DataStoreRepository(context = context)


    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

}