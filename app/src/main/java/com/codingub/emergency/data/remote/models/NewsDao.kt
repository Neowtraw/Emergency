package com.codingub.emergency.data.remote.models

import androidx.compose.runtime.Stable
import com.codingub.emergency.domain.models.News
import java.util.Date

@Stable
data class NewsDao(
    val imageUrl: String = "",
    val title: String = "",
    val description: String = "",
    val link: String = ""
) {

    fun toNews() =
        News(
            imageUrl = imageUrl,
            title = title,
            description = description,
            link = link
        )
}