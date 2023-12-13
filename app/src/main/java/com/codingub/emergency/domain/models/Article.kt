package com.codingub.emergency.domain.models

import androidx.compose.runtime.Immutable
import com.codingub.emergency.data.local.models.ArticleEntity

@Immutable
data class Article(
    val id: String = "",
    val title: String = "",
    val summary: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val videoUrl: String? = null,
    val phone: String? = null,
    val liked: Boolean = false
) {

    fun toArticleEntity() : ArticleEntity {
        return ArticleEntity(
            id = id,
            title = title,
            summary = summary,
            description = description,
            imageUrl = imageUrl,
            videoUrl = videoUrl,
            phone = phone,
            liked = liked
        )
    }
}