package com.codingub.emergency.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Article")
data class ArticleRef(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val imageUrl: String,
    val videoUrl: String?,
    val phone: String?,
    val alt: String,
    val liked: Boolean
)