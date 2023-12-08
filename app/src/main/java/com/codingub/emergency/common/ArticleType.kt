package com.codingub.emergency.common

import androidx.annotation.StringRes
import com.codingub.emergency.R

enum class ArticleType(@StringRes val title: Int) {
    ACCIDENTS(R.string.accidents),
    ILLNESSES(R.string.illness),
    WEATHER(R.string.weather)
}