package com.codingub.emergency.data.repos

import android.app.Activity
import com.codingub.emergency.core.ResultState
import com.codingub.emergency.domain.repos.AuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val db: FirebaseAuth,
    private val rep: DataStoreRepository
) : AuthRepository {

    private lateinit var omVerificationCode: String

    override fun createUserWithPhone(code: String, phone: String, activity: Activity): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading())

            val onVerificationCallback =
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) = Unit

                    override fun onVerificationFailed(p0: FirebaseException) {
                        trySend(ResultState.Error(p0))
                    }

                    override fun onCodeSent(
                        verificationCode: String,
                        p1: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(verificationCode, p1)
                        trySend(ResultState.Success("OTP Sent Successfully"))
                        CoroutineScope(Dispatchers.Main).launch {
                            rep.saveVerificationCode(verificationCode)
                        }
                    }
                }

            val options = PhoneAuthOptions.newBuilder(db)
                .setPhoneNumber("$code$phone")
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(onVerificationCallback)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)

            awaitClose {
                close()
            }
        }

    override fun signWithCredential(otp: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading())
        rep.readOnVerificationCode().collect {
            val credential = PhoneAuthProvider.getCredential(it, otp)
            db.signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) trySend(ResultState.Success("otp verified"))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(it))
                }
        }

        awaitClose {
            close()
        }
    }
}