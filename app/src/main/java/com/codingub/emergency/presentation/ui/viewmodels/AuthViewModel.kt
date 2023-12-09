package com.codingub.emergency.presentation.ui.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.codingub.emergency.domain.repos.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
  private val repository: AuthRepository
) : ViewModel(){

    fun createUserWithPhone(
        code: String,
        phoneNumber: String,
        activity: Activity
    ) = repository.createUserWithPhone(code, phoneNumber, activity)

    fun signInWithCredential(
        code: String
    ) = repository.signWithCredential(code)

}