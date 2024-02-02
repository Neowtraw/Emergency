package com.codingub.emergency.domain.models

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.codingub.emergency.data.local.models.ArticleEntity
import com.codingub.emergency.data.local.models.ArticleRef

@Stable
data class Article(
    val id: String = "",
    val title: String = "",
    val summary: String = "",
    val content: List<Content> = emptyList(),
    val imageUrl: String = "",
    val videoUrl: String? = null,
    val phone: String? = null,
    val alt: String = "",
    var liked: Boolean = false
) {

    fun toArticleRef() : ArticleRef = ArticleRef(
        id = id,
        title = title,
        summary = summary,
        imageUrl = imageUrl,
        videoUrl = videoUrl,
        phone = phone,
        alt = alt,
        liked = liked
    )

    fun toArticleEntity() : ArticleEntity {
        return ArticleEntity(
            article = ArticleRef(
                id = id,
                title = title,
                summary = summary,
                imageUrl = imageUrl,
                videoUrl = videoUrl,
                phone = phone,
                alt = alt,
                liked = liked
            ),
            content = content.map {
                it.toContentEntity()
            }
        )
    }
}