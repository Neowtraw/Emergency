package com.codingub.emergency.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codingub.emergency.domain.models.Content

@Entity(tableName = "Content")
data class ContentEntity(
    @PrimaryKey val id : String,
    val title: String?,
    val description: String,
    val imageUrl: String?,
    val articleId: String
) {

    fun toContent() = Content(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        articleId = articleId
    )
}