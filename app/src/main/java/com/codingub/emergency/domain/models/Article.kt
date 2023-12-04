package com.codingub.emergency.domain.models


data class Article(
    val title: String = "",
    val summary: String = "",
    val description: String = "",
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val phone: String? = null,

    val alt: String = "", // searching
    val liked: Boolean = false
)