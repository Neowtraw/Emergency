package com.codingub.emergency.presentation.ui.customs

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.codingub.emergency.R
import com.codingub.emergency.presentation.ui.theme.monFamily
import com.codingub.emergency.presentation.ui.utils.Constants

@Composable
fun HeaderText(
    @StringRes text: Int
) {
    Text(
        text = stringResource(id = text),
        modifier = Modifier
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = monFamily,
        color = colorResource(id = R.color.main_text),
        fontSize = Constants.MAIN_HEADER_TEXT.sp
    )
}

@Composable
fun AddAuthText(
    @StringRes text: Int
) {
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = stringResource(id = text),
        fontWeight = FontWeight.Medium,
        color = colorResource(id = R.color.add_text),
        textAlign = TextAlign.Center,
        fontFamily = monFamily,
        fontSize = Constants.MAIN_ADDITIONAL_TEXT.sp
    )
}

@Composable
fun InfoHeaderText(text: String, modifier: Modifier = Modifier) {
    Text(text = text,
        modifier = modifier,
        color = colorResource(id = R.color.main_text),
        fontSize = Constants.INFO_HEADER_TEXT.sp,
        fontFamily = monFamily,
        fontWeight = FontWeight.Medium)
}

@Composable
fun InfoContentText(text: String) {
    Text(text = text,
        color = colorResource(id = R.color.main_text),
        fontSize = Constants.MAIN_CONTENT_TEXT.sp,
        fontFamily = monFamily,
        fontWeight = FontWeight.Normal)
}

