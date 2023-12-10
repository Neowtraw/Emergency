package com.codingub.emergency.presentation.ui.screens.info

sealed class UserInfoEvent {
    data class UsernameChanged(val username: String) : UserInfoEvent()
    data class BirthdayChanged(val birthday: String) : UserInfoEvent()
    data class ParentPhoneChanged(val phone: String) : UserInfoEvent()
    data class AddressChanged(val address: String) : UserInfoEvent()

    object Submit : UserInfoEvent()

}