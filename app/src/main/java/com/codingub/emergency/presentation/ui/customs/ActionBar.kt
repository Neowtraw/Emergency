package com.codingub.emergency.presentation.ui.customs

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingub.emergency.R
import com.codingub.emergency.presentation.ui.theme.monFamily
import com.codingub.emergency.presentation.ui.utils.Constants


@Composable
fun ActionBar(@StringRes text: Int) {
    Text(
        text = stringResource(id = text),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = colorResource(id = R.color.main_text),
        fontFamily = monFamily,
        fontSize = Constants.INFO_HEADER_TEXT.sp,
        fontWeight = FontWeight.Normal
    )
    Spacer(modifier = Modifier.height(10.dp))
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .bottomShadow(5.dp),
        thickness = 0.dp, color = colorResource(
            id = R.color.divider
        )
    )
}


fun Modifier.bottomShadow(shadow: Dp) =
    this
        .clip(GenericShape { size, _ ->
            lineTo(size.width, 0f)
            lineTo(size.width, Float.MAX_VALUE)
            lineTo(0f, Float.MAX_VALUE)
        })
        .shadow(shadow)
