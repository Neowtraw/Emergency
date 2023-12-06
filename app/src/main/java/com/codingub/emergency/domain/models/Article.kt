package com.codingub.emergency.domain.models

import com.codingub.emergency.data.local.models.ArticleEntity
import com.codingub.emergency.data.local.models.FavoriteArticleEntity

data class Article(
    val id: String = "",
    val title: String = "",
    val summary: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val videoUrl: String? = null,
    val phone: String? = null,
) {

    fun toArticleEntity() : ArticleEntity {
        return ArticleEntity(
            id = id,
            title = title,
            summary = summary,
            description = description,
            imageUrl = imageUrl,
            videoUrl = videoUrl,
            phone = phone
        )
    }

    fun toFavoriteArticle() : FavoriteArticleEntity {
        return FavoriteArticleEntity(
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