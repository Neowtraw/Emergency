package com.codingub.emergency.presentation.ui.customs

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.codingub.emergency.R
import com.codingub.emergency.presentation.ui.utils.AssetUtil
import com.codingub.emergency.presentation.ui.utils.Constants

@Composable
fun MemoView(@StringRes headerText: Int, @StringRes contextText: Int) {
    val context = LocalContext.current
    InfoHeaderText(
        text = stringResource(id = headerText),
        color =  R.color.contrast
    )
    Spacer(modifier = Modifier.height(Constants.MAIN_PADDING.dp))
    InfoContentText(text = AssetUtil.getText(context, stringResource(id = contextText)))
    Spacer(modifier = Modifier.height(Constants.MAIN_PADDING.dp))
}