package com.codingub.emergency.presentation.ui.screens.info

data class UserInfoState(
    val username : String = "",
    val usernameError : String? = "",
    val birthday : String = "",
    val birthdayError : String? = "",
    val address : String = "",
    val addressError : String? = "",
    val parentNumber : String = "",
    val parentNumberError : String? = ""
)