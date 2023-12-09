package com.codingub.emergency.presentation.ui.screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.codingub.emergency.R
import com.codingub.emergency.common.Country
import com.codingub.emergency.common.ResultState
import com.codingub.emergency.presentation.navigation.NavRoute.USER_VERIFICATION
import com.codingub.emergency.presentation.ui.customs.AddAuthText
import com.codingub.emergency.presentation.ui.customs.FinishButton
import com.codingub.emergency.presentation.ui.customs.HeaderText
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserAuthScreen(
    navController: NavController,
    activity: Activity,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.call))
    var code by rememberSaveable { mutableStateOf(Country.Belarus) }
    var mask by rememberSaveable { mutableStateOf("${code.code} (00) 000 00 00") }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = colorResource(id = R.color.background)
            )
            .padding(MAIN_PADDING.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MAIN_PADDING.dp)
                .verticalScroll(rememberScrollState(), reverseScrolling = true),
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
            CountryDropDownMenu(code) {code = it
            mask = "${code.code} (00) 000 00 00"}
            Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))

            FinishButton(
                modifier = Modifier.padding(top = 50.dp),
                text = R.string.finish,
                visible = {
                    phoneNumber.count() == 9
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
                                    //     isDialog = false
                                    Log.d("user", it.data.toString())
                                    navController.navigate(USER_VERIFICATION)

                                }

                                is ResultState.Error -> {
                                    //    isDialog = false
                                    activity.showMsg(it.error.toString())
                                    Log.d("userErr", it.error.toString())

                                }

                                is ResultState.Loading -> {
                                    //     isDialog = true
                                }
                            }
                        }
                    }
                }
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDropDownMenu(
    code: Country,
    onCountryPicked: (Country) -> Unit
) {
    var isExpended by rememberSaveable {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(expanded = isExpended,
        onExpandedChange = {
            isExpended = it
        }) {
        OutlinedTextField(
            value =  code.code, onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpended)
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = code.flag),
                    contentDescription = "Country Flag"
                )
            },
            label = {
                Text(
                    stringResource(id = R.string.country_code),
                    color = colorResource(id = R.color.main_text)
                )
            },
            modifier = Modifier.menuAnchor(),
            shape = RoundedCornerShape(MAIN_CORNER.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = R.color.main_text),
                unfocusedBorderColor = colorResource(id = R.color.add_text)
            )
        )

        ExposedDropdownMenu(expanded = isExpended,
            modifier = Modifier
                .background(color = colorResource(id = R.color.background)),
            onDismissRequest = {
                isExpended = false
            }) {
            Country.values().forEach { country ->
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = CenterVertically
                        ){
                            Image(
                                painter = painterResource(id = country.flag),
                                contentDescription = "Country Flag"
                            )
                            Text(text = country.name)
                        }

                    },
                    onClick = {
                        onCountryPicked(country)
                        isExpended = false
                    }
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
        value = phoneNumber,
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
