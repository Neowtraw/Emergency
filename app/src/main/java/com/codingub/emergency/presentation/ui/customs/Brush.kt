package com.codingub.emergency.presentation.ui.customs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import com.codingub.emergency.R

@Composable
fun getBackgroundBrush() : Brush {
    return Brush.verticalGradient(
        colors = listOf(colorResource(id = R.color.background_first), colorResource(id = R.color.background_second)), // Замените цвета на свои
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )
}
