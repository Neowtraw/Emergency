package com.codingub.emergency.presentation.ui.screens

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codingub.emergency.MainActivity
import com.codingub.emergency.di.RoomModule
import com.codingub.emergency.presentation.navigation.NavRoute.ARTICLE_BOARDING
import com.codingub.emergency.presentation.ui.theme.EmergencyTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
@UninstallModules(RoomModule::class)
class ArticleScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            val navController = rememberNavController()
            EmergencyTheme {
                NavHost(navController = navController,
                    startDestination = ARTICLE_BOARDING) {
                    composable()
                }
            }
        }
    }


}