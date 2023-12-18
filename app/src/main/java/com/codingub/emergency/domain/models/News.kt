package com.codingub.emergency.domain.models

import androidx.compose.runtime.Stable

@Stable
data class News(
    val imageUrl: String = "",
    val title: String = "",
    val description: String = "",
    val link: String = ""
) {

    fun isEmpty() = link.isEmpty() && imageUrl.isEmpty()
            && title.isEmpty() && description.isEmpty()
}