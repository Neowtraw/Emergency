package com.codingub.emergency.presentation.ui.screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.codingub.emergency.R
import com.codingub.emergency.core.Country
import com.codingub.emergency.core.ResultState
import com.codingub.emergency.presentation.ui.customs.AddAuthText
import com.codingub.emergency.presentation.ui.customs.CountryDropDownMenu
import com.codingub.emergency.presentation.ui.customs.FinishButton
import com.codingub.emergency.presentation.ui.customs.HeaderText
import com.codingub.emergency.presentation.ui.customs.getBackgroundBrush
import com.codingub.emergency.presentation.ui.theme.EmergencyTheme
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_CORNER
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_DIVIDER
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_DIVIDER_ITEMS
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_PADDING
import com.codingub.emergency.presentation.ui.utils.PhoneUtil
import com.codingub.emergency.presentation.ui.utils.showMsg
import com.codingub.emergency.presentation.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun UserAuthScreen(
    onAuthFinished: () -> Unit,
    activity: Activity
) {
    val viewModel: AuthViewModel = hiltViewModel()

    var phoneNumber by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.call))
    var code by rememberSaveable { mutableStateOf(Country.Belarus) }
    var mask by rememberSaveable { mutableStateOf("${code.code} ${code.pattern}") }
    var loading by rememberSaveable { mutableStateOf(false) }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(getBackgroundBrush())

                .padding(MAIN_PADDING.dp)
                .clickable(enabled = !loading) {},
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            LottieAnimation(
                modifier = Modifier.size(100.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )

            HeaderText(text = R.string.your_phone_number)
            Spacer(modifier = Modifier.height(MAIN_DIVIDER_ITEMS.dp))
            AddAuthText(text = R.string.your_phone_number_add)
            Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))
            PhoneField(phoneNumber = phoneNumber, mask = mask) {
                phoneNumber = it
            }
            Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))
            CountryDropDownMenu(code = code) {
                code = it
                mask = "${code.code} ${code.pattern}"
            }
            Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))

            FinishButton(
                modifier = Modifier.padding(top = 50.dp),
                text = R.string.finish,
                visible = {
                    phoneNumber.count() >= 9
                },
                onClick = {
                    scope.launch(Dispatchers.Main) {
                        viewModel.createUserWithPhone(
                            code.code,
                            phoneNumber,
                            activity
                        ).collect {
                            when (it) {
                                is ResultState.Success -> {
                                    loading = false
                                    Log.d("user", it.data.toString())
                                    onAuthFinished()
                                }

                                is ResultState.Error -> {
                                    loading = false
                                    activity.showMsg(it.error.toString())
                                    Log.d("userErr", it.error.toString())

                                }

                                is ResultState.Loading -> {
                                    loading = true
                                }
                            }
                        }
                    }
                }
            )
        }

        if(loading) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.background_loading)),
                contentAlignment = Alignment.Center) {

                CircularProgressIndicator(
                    color = colorResource(id = R.color.contrast)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneField(
    phoneNumber: String,
    maskNumber: Char = '0',
    mask: String,
    onTextChange: (String) -> Unit,
) {

    OutlinedTextField(
        value = if(mask.length < phoneNumber.length) phoneNumber.substring(0, mask.length) else phoneNumber,
        onValueChange = { number ->
            onTextChange(number.take(mask.count { it == maskNumber }))
        },
        label = {
            Text(
                stringResource(id = R.string.phone_number),
                color = colorResource(id = R.color.main_text)
            )
        },
        maxLines = 1,
        visualTransformation = PhoneUtil(mask, maskNumber),
        textStyle = TextStyle(color = colorResource(id = R.color.main_text)),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(MAIN_CORNER.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.main_text),
            unfocusedBorderColor = colorResource(id = R.color.add_text)
        )
    )
}


@Composable
@Preview(device = "id:pixel_4a", showBackground = true)
private fun MainScreenPreview() {

    EmergencyTheme {
    }
}
