package com.codingub.emergency.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codingub.emergency.data.local.models.ArticleEntity
import com.codingub.emergency.data.local.models.ServiceEntity

@Database(
    entities = [ArticleEntity::class, ServiceEntity::class],
    version = 3
)
abstract class RoomDatabase : RoomDatabase() {

    abstract fun dao(): RoomDao
}