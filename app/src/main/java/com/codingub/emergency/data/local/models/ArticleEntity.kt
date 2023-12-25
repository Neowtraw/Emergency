package com.codingub.emergency.data.local.models

import androidx.room.Embedded
import androidx.room.Relation
import com.codingub.emergency.domain.models.Article

data class ArticleEntity(
    @Embedded val article: ArticleRef,
    @Relation(
        parentColumn = "id",
        entityColumn = "articleId",
        entity = ContentEntity::class
    ) val content: List<ContentEntity>
) {

    fun toArticle(): Article {
        return Article(
            id = article.id,
            title = article.title,
            summary = article.summary,
            content = content.map {
                it.toContent()
            },
            imageUrl = article.imageUrl,
            videoUrl = article.videoUrl,
            phone = article.phone,
            liked = article.liked
        )
    }
}