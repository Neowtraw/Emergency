package com.codingub.emergency.presentation.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingub.emergency.data.repos.DataStoreRepository
import com.codingub.emergency.presentation.navigation.NavRoute.HOME
import com.codingub.emergency.presentation.navigation.NavRoute.WELCOME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val repository: DataStoreRepository
) : ViewModel(){

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _startDestination: MutableState<String> = mutableStateOf(HOME)
    val startDestination: State<String> = _startDestination

    init {
        viewModelScope.launch {
            repository.readOnBoardingState().collect { completed ->
                if (completed) {
                    _startDestination.value = HOME
                } else {
                    _startDestination.value = WELCOME
                }

                withContext(Dispatchers.Main) {
                    delay(700)
                    _isLoading.value = false

                }
            }
        }
    }
}