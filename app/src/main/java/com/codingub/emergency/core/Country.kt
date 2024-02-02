package com.codingub.emergency.core

import androidx.annotation.DrawableRes
import com.codingub.emergency.R

enum class Country(val code: String, val language: String,val pattern: String, @DrawableRes val flag: Int) {
    Russia("+7","ru", "(000) 000 0000", R.drawable.russia),
    Poland("+48","pl", "(000) 000 0000", R.drawable.poland),
    Belarus("+375","be","(00) 000 0000", R.drawable.belarus)
}