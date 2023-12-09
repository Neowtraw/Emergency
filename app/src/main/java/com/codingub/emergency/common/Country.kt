package com.codingub.emergency.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.codingub.emergency.R

enum class Country(val code: String, @DrawableRes val flag: Int) {
    Russia("+7", R.drawable.russia),
    Poland("+48", R.drawable.poland),
    Belarus("+375", R.drawable.belarus)
}