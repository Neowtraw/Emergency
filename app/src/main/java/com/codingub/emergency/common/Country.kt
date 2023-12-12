package com.codingub.emergency.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.codingub.emergency.R

enum class Country(val code: String, val language: String, @DrawableRes val flag: Int) {
    Russia("+7","ru", R.drawable.russia),
    Poland("+48","pl", R.drawable.poland),
    Belarus("+375","be", R.drawable.belarus)
}