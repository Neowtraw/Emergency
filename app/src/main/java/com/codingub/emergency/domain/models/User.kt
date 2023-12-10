package com.codingub.emergency.domain.models

import androidx.compose.runtime.Immutable

@Immutable
data class User(
    val username: String = "",
    val phone: String = "",
    val age: Int = -1,
    val address: String = "",
    val parentNumber: String? = null
)