package com.codingub.emergency.core

import androidx.annotation.StringRes
import com.codingub.emergency.R

enum class ArticleType(@StringRes val title: Int) {
    Accidents(R.string.accidents),
    Illnesses(R.string.illness),
    Weather(R.string.weather)
}