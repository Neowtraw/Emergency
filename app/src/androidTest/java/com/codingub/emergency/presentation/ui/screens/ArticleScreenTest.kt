package com.codingub.emergency.presentation.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codingub.emergency.MainActivity
import com.codingub.emergency.di.RoomModule
import com.codingub.emergency.presentation.navigation.NavRoute.ARTICLES
import com.codingub.emergency.presentation.navigation.NavRoute.ARTICLE_INFO
import com.codingub.emergency.presentation.ui.theme.EmergencyTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
                NavHost(
                    navController = navController,
                    startDestination = ARTICLES
                ) {

                    composable(route = ARTICLES) {
                        ArticleScreen { navController.navigate(ARTICLE_INFO)}
                        ArticleInfoScreen(id = "bleeding") {}
                    }

                }
            }
        }
    }

    @Test
    fun changeTextInInput() {
        composeRule.onNodeWithTag("search").performTextInput("Accidents")
        composeRule.onNodeWithTag("list").assertIsDisplayed()
        composeRule.onNodeWithTag("list").performClick()
    }


}