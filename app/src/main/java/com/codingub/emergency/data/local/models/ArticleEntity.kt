package com.codingub.emergency.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codingub.emergency.domain.models.Article

@Entity(tableName = "Article")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val description: String,
    val imageUrl: String,
    val videoUrl: String?,
    val phone: String?
) {

    fun toArticle() : Article {
        return Article(
            id = id,
            title = title,
            summary = summary,
            description = description,
            imageUrl = imageUrl,
            videoUrl = videoUrl,
            phone = phone
        )
    }
}