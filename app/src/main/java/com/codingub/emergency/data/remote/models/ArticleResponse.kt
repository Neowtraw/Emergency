package com.codingub.emergency.data.remote.models

import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.domain.models.Content

data class ArticleResponse(
    val id: String,
    val item: ArticleItem,
    val content: List<ContentResponse>
    ) {

    data class ArticleItem(
        val title: String = "",
        val summary: String = "",
        val imageUrl: String = "",
        val videoUrl: String? = null,
        val phone: String? = null,
        val alt: String = ""
    )

    fun toArticle(): Article {
        return Article(
            id = id,
            title = item.title,
            summary = item.summary,
            content = content.map {
                Content(
                    id = it.id,
                    title = it.item.title,
                    description = it.item.description,
                    imageUrl = it.item.imageUrl,
                    articleId = id
                )
            },
            imageUrl = item.imageUrl,
            videoUrl = item.videoUrl,
            alt = item.alt,
            phone = item.phone
        )
    }
}

data class ContentResponse(
    val id: String,
    val item: ContentItem
) {
    data class ContentItem(
        val title: String? = null,
        val description: String = "",
        val imageUrl: String? = ""
    )
}
