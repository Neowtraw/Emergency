package com.codingub.emergency.presentation.ui.customs

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingub.emergency.R
import com.codingub.emergency.presentation.ui.theme.monFamily
import com.codingub.emergency.presentation.ui.utils.Constants

@Composable
fun ErrorStateView(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int = R.drawable.ic_error,
    @StringRes message: Int = R.string.exception_unknown_error
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = null,
            tint = colorResource(id = R.color.contrast)
        )
        Spacer(modifier = Modifier.height(Constants.MAIN_DIVIDER.dp))

        Text(
            text = stringResource(id = message),
            modifier = Modifier
                .fillMaxWidth(),
            fontWeight = FontWeight.Light,
            color = colorResource(id = R.color.warn_text),
            textAlign = TextAlign.Center,
            fontFamily = monFamily,
            fontSize = Constants.MAIN_ADDITIONAL_TEXT.sp
        )
    }
}

@Composable
fun ErrorStateView(
    message: String,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int = R.drawable.ic_error
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = null,
            tint = colorResource(id = R.color.contrast)
        )
        Spacer(modifier = Modifier.height(Constants.MAIN_DIVIDER.dp))

        Text(
            text = message,
            modifier = Modifier
                .fillMaxWidth(),
            fontWeight = FontWeight.Light,
            color = colorResource(id = R.color.warn_text),
            textAlign = TextAlign.Center,
            fontFamily = monFamily,
            fontSize = Constants.MAIN_ADDITIONAL_TEXT.sp
        )
    }
}


@Composable
fun EmptyStateView(
    message: String = ""
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = message,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            fontWeight = FontWeight.Light,
            color = colorResource(id = R.color.warn_text),
            textAlign = TextAlign.Center,
            fontFamily = monFamily,
            fontSize = Constants.MAIN_ADDITIONAL_TEXT.sp
        )
    }
}
