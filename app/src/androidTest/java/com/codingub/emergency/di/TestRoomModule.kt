package com.codingub.emergency.di

import android.content.Context
import androidx.room.Room
import com.codingub.emergency.data.local.room.RoomDao
import com.codingub.emergency.data.local.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TestRoomModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context) : RoomDatabase {
        return Room.inMemoryDatabaseBuilder(
            context, RoomDatabase::class.java
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideAppDao(
        database: RoomDatabase
    ): RoomDao = database.dao()
}