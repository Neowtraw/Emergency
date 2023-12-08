package com.codingub.emergency.presentation.ui.utils

sealed class ScreenState<out T> {
    object Loading : ScreenState<Nothing>()
    data class Success<T>(val data: T) : ScreenState<T>()
    data class Error(val error: Throwable) : ScreenState<Nothing>()
}