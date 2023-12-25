package com.codingub.emergency.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codingub.emergency.data.local.models.ArticleRef
import com.codingub.emergency.data.local.models.ContentEntity
import com.codingub.emergency.data.local.models.ServiceEntity

@Database(
    entities = [ArticleRef::class, ServiceEntity::class, ContentEntity::class],
    version = 5,
    exportSchema = false
)
abstract class RoomDatabase : RoomDatabase() {

    abstract fun dao(): RoomDao
}