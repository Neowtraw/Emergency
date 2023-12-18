package com.codingub.emergency.domain.repos

import android.app.Activity
import com.codingub.emergency.core.ResultState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun createUserWithPhone(
        code: String,
        phone: String,
        activity: Activity
    ) : Flow<ResultState<String>>

    fun signWithCredential(
        otp: String
    ): Flow<ResultState<String>>
}