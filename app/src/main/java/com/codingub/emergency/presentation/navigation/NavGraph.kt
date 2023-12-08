package com.codingub.emergency.presentation.navigation

import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codingub.emergency.presentation.ui.screens.ArticleScreen
import com.codingub.emergency.presentation.ui.screens.HomeScreen
import com.codingub.emergency.presentation.ui.screens.InfoScreen
import com.codingub.emergency.presentation.ui.screens.UserAuthScreen
import com.codingub.emergency.presentation.ui.screens.UserInfoScreen
import com.codingub.emergency.presentation.ui.screens.UserVerificationScreen
import com.codingub.emergency.presentation.ui.screens.WelcomeScreen
import com.google.firebase.auth.UserInfo

@Composable
fun setupNavGraph(
    navController: NavHostController,
    startDestination: String,
    padding: PaddingValues,
    activity: Activity
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = androidx.compose.ui.Modifier.padding(padding)
    ) {
        composable(route = NavRoute.ARTICLES) {
            ArticleScreen(
                navController = navController
            )
        }
        composable(route = NavRoute.HOME) {
            HomeScreen(
                navController = navController
            )
        }
        composable(route = NavRoute.INFO) {
            InfoScreen(
                navController = navController
            )
        }
        composable(route = NavRoute.WELCOME) {
            WelcomeScreen(
                navController = navController
            )
        }
        composable(route = NavRoute.WELCOME) {
            WelcomeScreen(
                navController = navController
            )
        }
        composable(route = NavRoute.USER_AUTH) {
            UserAuthScreen(
                navController = navController,
                activity = activity
            )
        }
        composable(route = NavRoute.USER_INFO) {
            UserInfoScreen(
                navController = navController
            )
        }
        composable(route = NavRoute.USER_VERIFICATION) {
            UserVerificationScreen(
                navController = navController)
        }
    }

}