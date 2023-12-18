package com.codingub.emergency.presentation.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingub.emergency.data.repos.DataStoreRepository
import com.codingub.emergency.domain.use_cases.SetUserInformation
import com.codingub.emergency.domain.use_cases.validation.ValidateBirthday
import com.codingub.emergency.domain.use_cases.validation.ValidateGeneral
import com.codingub.emergency.domain.use_cases.validation.ValidateParentNumber
import com.codingub.emergency.presentation.ui.screens.info.UserInfoEvent
import com.codingub.emergency.presentation.ui.screens.info.UserInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DateTimeException
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val setUserInformation: SetUserInformation,
    private val validateGeneral: ValidateGeneral,
    private val validateParentNumber: ValidateParentNumber,
    private val validateBirthday: ValidateBirthday,
    private val repository: DataStoreRepository
) : ViewModel() {

    var state by mutableStateOf(UserInfoState())


    private val validationChannel = Channel<ValidationEvent>()
    val validationEvents = validationChannel.receiveAsFlow()

    fun onEvent(event: UserInfoEvent) {
        when (event) {
            is UserInfoEvent.UsernameChanged -> {
                state = state.copy(username = event.username)
            }

            is UserInfoEvent.BirthdayChanged -> {
                state = state.copy(birthday = event.birthday)
            }

            is UserInfoEvent.AddressChanged -> {
                state = state.copy(address = event.address)
            }

            is UserInfoEvent.ParentPhoneChanged -> {
                state = state.copy(parentNumber = event.phone)
            }

            is UserInfoEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        var isDateValid = false
        var age = -1

        val formattedDate = state.birthday.getFormattedDate()
        if (formattedDate != null) {
            val (day, month, year) = formattedDate
            isDateValid = isDateValid(day, month, year)

            if (isDateValid) {
                age = getAge(year, month, day)
            }
        }

        val userResult = validateGeneral.execute(state.username)
        val addressResult = validateGeneral.execute(state.address)
        val birthdayResult = validateBirthday.execute(isDateValid)
        val parentNumberResult = validateParentNumber.execute(state.parentNumber, age)

        val hasError = listOf(
            userResult,
            addressResult,
            birthdayResult,
            parentNumberResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                usernameError = userResult.errorMessage,
                birthdayError = birthdayResult.errorMessage,
                addressError = addressResult.errorMessage,
                parentNumberError = parentNumberResult.errorMessage
            )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            setUserInformation(
                username = state.username,
                address = state.address,
                parentPhone = state.parentNumber.ifBlank { null },
                age = age
            )
            validationChannel.send(ValidationEvent.Success)
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}

fun isDateValid(day: Int, month: Int, year: Int): Boolean {
    return try {
        val date = LocalDate.of(year, month, day)
        val currentDate = LocalDate.now()
        date <= currentDate
    } catch (ex: DateTimeException) {
        false
    }
}

fun String.getFormattedDate(): Triple<Int, Int, Int>? {
    if (this.length < 8 || this.length > 8) return null
    val day = this.substring(0, 2).toIntOrNull() ?: 1
    val month = this.substring(2, 4).toIntOrNull() ?: 1
    val year = this.substring(4, 8).toIntOrNull() ?: 2023
    return Triple(day, month, year)
}

fun getAge(year: Int, month: Int, dayOfMonth: Int): Int {
    return Period.between(
        LocalDate.of(year, month, dayOfMonth),
        LocalDate.now()
    ).years
}