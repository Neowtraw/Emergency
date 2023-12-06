package com.codingub.emergency.presentation.ui.utils

import androidx.annotation.RawRes
import com.codingub.emergency.R

sealed class OnBoardingPage(
    @RawRes
    val animation: Int,
    val title: String,
    val description: String
) {

    object FirstPage : OnBoardingPage(
        animation = R.raw.first_page,
        title = "Полезные знания",
        description = "Узнай как правильно вести себя в экстренных ситуациях"
    )

    object SecondPage : OnBoardingPage(
        animation = R.raw.second_page,
        title = "Выход из экстренных ситуаций",
        description = "Обратись через приложение в службы экстренного спасения"
    )

    object ThirdPage : OnBoardingPage(
        animation = R.raw.third_page,
        title = "Дополнительная помощь",
        description = "Внеси корректную информацию о себе, чтобы улучшить свою безопасность"
    )

}
