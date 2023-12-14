package com.codingub.emergency.presentation.ui.screens.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.codingub.emergency.R
import com.codingub.emergency.presentation.navigation.NavRoute.HOME
import com.codingub.emergency.presentation.ui.customs.AddAuthText
import com.codingub.emergency.presentation.ui.customs.FinishButton
import com.codingub.emergency.presentation.ui.customs.HeaderText
import com.codingub.emergency.presentation.ui.customs.getBackgroundBrush
import com.codingub.emergency.presentation.ui.theme.monFamily
import com.codingub.emergency.presentation.ui.utils.Constants
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_CONTENT_TEXT
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_LABEL_TEXT
import com.codingub.emergency.presentation.ui.viewmodels.UserInfoViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UserInfoScreen(
    navController: NavController,
    userInfoViewModel: UserInfoViewModel = hiltViewModel()
) {


    val state = userInfoViewModel.state
    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        userInfoViewModel.validationEvents.collectLatest { event ->
            when (event) {
                is UserInfoViewModel.ValidationEvent.Success -> {
                    navController.navigate(HOME)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState(), reverseScrolling = true)
            .fillMaxSize()
            .background(brush = getBackgroundBrush())
            .statusBarsPadding()
            .padding(horizontal = 30.dp)
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.auth_background),
            contentDescription = "Auth Background",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 10.dp)
        )

        HeaderText(text = R.string.authorization)
        //      Spacer(modifier = Modifier.height(Constants.MAIN_DIVIDER_ITEMS.dp))
        AddAuthText(text = R.string.authorization_add)
        //     Spacer(modifier = Modifier.height(Constants.MAIN_DIVIDER_ITEMS.dp))

        if (state.usernameError != null) {
            Text(
                text = state.usernameError, color = colorResource(id = R.color.error_text),
                modifier = Modifier.align(Alignment.End),
                fontFamily = monFamily,
                fontSize = MAIN_LABEL_TEXT.sp
            )
        }
        UserText(
            str = state.username, label = "Username",
            keyboardOptions = KeyboardOptions(autoCorrect = true, keyboardType = KeyboardType.Text),
            maxLines = 1,
            maxLength = 40
        ) {
            userInfoViewModel.onEvent(UserInfoEvent.UsernameChanged(it))
        }
        //      Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))
        if (state.addressError != null) {
            Text(
                text = state.addressError, color = colorResource(id = R.color.error_text),
                modifier = Modifier.align(Alignment.End),
                fontFamily = monFamily,
                fontSize = MAIN_LABEL_TEXT.sp
            )
        }
        UserText(
            str = state.address, label = "Address",
            keyboardOptions = KeyboardOptions(autoCorrect = true, keyboardType = KeyboardType.Text),
            maxLines = 3,

            maxLength = 100
        ) {
            userInfoViewModel.onEvent(UserInfoEvent.AddressChanged(it))
        }
        //    Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))
        if (state.birthdayError != null) {
            Text(
                text = state.birthdayError, color = colorResource(id = R.color.error_text),
                modifier = Modifier.align(Alignment.End),
                fontSize = MAIN_LABEL_TEXT.sp
            )
        }
        BirthdayView(modifier = Modifier
            .align(Alignment.CenterHorizontally),
            birthday = state.birthday,
            onTextChange = {
                userInfoViewModel.onEvent(UserInfoEvent.BirthdayChanged(it))
            })
        //    Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))
        if (state.parentNumberError != null) {
            Text(
                text = state.parentNumberError, color = colorResource(id = R.color.error_text),
                modifier = Modifier.align(Alignment.End),
                fontFamily = monFamily,
                fontSize = MAIN_LABEL_TEXT.sp
            )
        }
        UserText(
            str = state.parentNumber, label = "Parent Phone",
            keyboardOptions = KeyboardOptions(
                autoCorrect = true,
                keyboardType = KeyboardType.Phone
            ),
            maxLines = 1,
            maxLength = 11
        ) {
            userInfoViewModel.onEvent(UserInfoEvent.ParentPhoneChanged(it))
        }
        Text(
            text = "Optional", color = colorResource(id = R.color.add_text),
            fontFamily = monFamily,
            fontSize = MAIN_LABEL_TEXT.sp
        )

        FinishButton(
            modifier = Modifier.padding(top = 50.dp),
            text = R.string.finish,
            visible = { true },
            onClick = {
                userInfoViewModel.onEvent(UserInfoEvent.Submit)
            }
        )

    }

}

@Composable
fun BirthdayView(
    modifier: Modifier = Modifier,
    birthday: String,
    onTextChange: (String) -> Unit,
    enabled: Boolean = true,
) {

    BasicTextField(
        modifier = modifier,
        value = birthday,
        onValueChange = {
            onTextChange.invoke(it.take(8))

        },
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        decorationBox = {
            Column {
                Text(
                    text = "Birthday",
                    fontFamily = monFamily,
                    fontSize = MAIN_LABEL_TEXT.sp,
                    color = colorResource(id = R.color.add_text)
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CellView(index = arrayOf(0, 1), text = birthday, size = 2) // day
                    Spacer(modifier = Modifier.width(10.dp))
                    CellView(index = arrayOf(2, 3), text = birthday, size = 2) // month
                    Spacer(modifier = Modifier.width(10.dp))
                    CellView(index = arrayOf(4, 5, 6, 7), text = birthday, size = 4) // year

                }
            }
        },
    )
}

@Composable
fun CellView(
    modifier: Modifier = Modifier,
    index: Array<Int>,
    text: String,
    charSize: TextUnit = 20.sp,
    size: Int = 2
) {
    Column(
        modifier = modifier
            .height(charSize.value.dp * 2)
            .width(charSize.value.dp * size)
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.text_field_outline),
                shape = MaterialTheme.shapes.medium
            )
            .padding(bottom = 4.dp)
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val str = buildString {
            for (i in index.indices) {
                if (index[i] >= text.length) append("")
                else append(text[index[i]])
            }
        } //with max text line
        Text(
            text = str,
            color = colorResource(id = R.color.main_text),
            modifier = modifier.wrapContentHeight(),
            fontFamily = monFamily,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = MAIN_CONTENT_TEXT.sp,
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserText(
    modifier: Modifier = Modifier,
    str: String,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    maxLength: Int = 0,
    maxLines: Int = 1,
    onTextChange: (String) -> Unit
) {
    OutlinedTextField(
        value = str,
        modifier = modifier.fillMaxWidth(),
        textStyle = TextStyle(
            color = colorResource(id = R.color.main_text),
            fontFamily = monFamily,
        ),
        keyboardOptions = keyboardOptions,

        label = {
            Text(
                text = label, color = colorResource(id = R.color.add_text), fontFamily = monFamily,
            )
        },
        shape = RoundedCornerShape(Constants.MAIN_CORNER.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.text_field_outline),
            unfocusedBorderColor = colorResource(id = R.color.text_field_outline)
        ),
        onValueChange = {
            onTextChange(it.take(maxLength))

        },
        maxLines = maxLines
    )
}
