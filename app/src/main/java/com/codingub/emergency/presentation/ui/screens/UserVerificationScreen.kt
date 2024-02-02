package com.codingub.emergency.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codingub.emergency.R
import com.codingub.emergency.core.ResultState
import com.codingub.emergency.presentation.ui.customs.AddAuthText
import com.codingub.emergency.presentation.ui.customs.FinishButton
import com.codingub.emergency.presentation.ui.customs.HeaderText
import com.codingub.emergency.presentation.ui.customs.OtpView
import com.codingub.emergency.presentation.ui.customs.getBackgroundBrush
import com.codingub.emergency.presentation.ui.utils.Constants
import com.codingub.emergency.presentation.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UserVerificationScreen(
    onVerificationFinished: () -> Unit
) {
    val viewModel: AuthViewModel = hiltViewModel()


    var otp by rememberSaveable { mutableStateOf(("")) }
    val scope = rememberCoroutineScope()
    var loading by rememberSaveable { mutableStateOf(false) }


    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(getBackgroundBrush())
                .padding(horizontal = 20.dp)
                .clickable(enabled = !loading) {},
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                HeaderText(text = R.string.your_phone_number)
                Spacer(modifier = Modifier.height(Constants.MAIN_DIVIDER_ITEMS.dp))
                AddAuthText(text = R.string.verification_code_request)
                Spacer(modifier = Modifier.height(50.dp))

                OtpView(
                    modifier = Modifier, otpText = otp
                ) {
                    otp = it
                }
                FinishButton(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                    text = R.string.finish,
                    visible = { otp.count() == 6 },
                    onClick = {
                        scope.launch(Dispatchers.Main) {
                            viewModel.signInWithCredential(otp).collect {
                                when (it) {
                                    is ResultState.Success -> {
                                        loading = false
                                        Log.d("user", "successful")
                                        onVerificationFinished()
                                    }

                                    is ResultState.Error -> {
                                        loading = false
                                        Log.d("user", "error")

                                    }

                                    is ResultState.Loading -> {
                                        loading = true
                                        Log.d("user", "loading")
                                    }
                                }
                            }

                        }
                    }
                )
            }
        }

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.background_loading)),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator(
                    color = colorResource(id = R.color.contrast)
                )
            }
        }
    }
}