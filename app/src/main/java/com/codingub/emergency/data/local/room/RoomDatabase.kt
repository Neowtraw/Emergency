package com.codingub.emergency.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codingub.emergency.data.local.models.ArticleEntity
import com.codingub.emergency.data.local.models.FavoriteArticleEntity
import com.codingub.emergency.data.local.models.ServiceEntity

@Database(
    entities = [ArticleEntity::class, FavoriteArticleEntity::class, ServiceEntity::class],
    version = 2
)
abstract class RoomDatabase : RoomDatabase() {

    abstract fun dao(): RoomDao
}