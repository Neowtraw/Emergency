package com.codingub.emergency.domain.models

import com.codingub.emergency.data.local.models.ContentEntity

data class Content(
    val id: String,
    val title: String? = null,
    val description: String = "",
    val imageUrl: String? = null,
    val articleId: String
) {

    fun toContentEntity() = ContentEntity(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        articleId = articleId
    )
}
