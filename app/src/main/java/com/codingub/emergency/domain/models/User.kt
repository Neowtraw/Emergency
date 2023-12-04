package com.codingub.emergency.domain.models

data class User(
    val username: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val age: Int = -1,
    val parentNumber: String? = null
)