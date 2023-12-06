package com.codingub.emergency.data.remote.responses

import com.codingub.emergency.domain.models.Article

data class ArticleResponse(
    val id: String,
    val item: ArticleItem
) {

    data class ArticleItem(
        val title: String = "",
        val summary: String = "",
        val description: String = "",
        val imageUrl: String = "",
        val videoUrl: String? = null,
        val phone: String? = null,
        val alt: String = ""
    )

    fun toArticle() : Article {
        return Article(
            id = id,
            title = item.title,
            summary = item.summary,
            description = item.description,
            imageUrl = item.imageUrl,
            videoUrl = item.videoUrl,
            phone = item.phone
        )
    }
}