package com.codingub.emergency.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codingub.emergency.presentation.ui.screens.ArticleScreen
import com.codingub.emergency.presentation.ui.screens.HomeScreen
import com.codingub.emergency.presentation.ui.screens.WelcomeScreen

@Composable
fun setupNavGraph(
    navController: NavHostController,
    startDestination: String,
    padding: PaddingValues
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
            ArticleScreen(
                navController = navController
            )
        }
        composable(route = NavRoute.WELCOME) {
            WelcomeScreen(
                navController = navController
            )
        }
    }

}