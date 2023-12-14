package com.codingub.emergency.presentation.navigation

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.codingub.emergency.presentation.ui.screens.ArticleInfoScreen
import com.codingub.emergency.presentation.ui.screens.ArticleScreen
import com.codingub.emergency.presentation.ui.screens.HomeScreen
import com.codingub.emergency.presentation.ui.screens.InfoScreen
import com.codingub.emergency.presentation.ui.screens.UserAuthScreen
import com.codingub.emergency.presentation.ui.screens.UserVerificationScreen
import com.codingub.emergency.presentation.ui.screens.WelcomeScreen
import com.codingub.emergency.presentation.ui.screens.info.UserInfoScreen
import com.codingub.emergency.presentation.ui.viewmodels.SharedViewModel

@RequiresApi(Build.VERSION_CODES.Q)
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
        composable(route = NavRoute.INFO) {
            InfoScreen()
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
                navController = navController
            )
        }



        navigation(
            startDestination = NavRoute.ARTICLES,
            route = NavRoute.ARTICLE_BOARDING
        ) {

            composable(route = NavRoute.ARTICLE_INFO) { entry ->
                val viewModel =
                    entry.sharedViewModel<SharedViewModel>(navController = navController)
                val state by viewModel.sharedState.collectAsStateWithLifecycle()

                ArticleInfoScreen(
                    navController = navController,
                    id = state
                )
            }

            composable(route = NavRoute.ARTICLES) { entry ->
                val viewModel =
                    entry.sharedViewModel<SharedViewModel>(navController = navController)
               // val state by viewModel.sharedState.collectAsStateWithLifecycle()

                ArticleScreen(
                    onArticleClicked = {
                        viewModel.updateState(id = it)
                        navController.navigate(NavRoute.ARTICLE_INFO)
                    }
                )
            }
        }

        navigation(
            startDestination = NavRoute.HOME,
            route = NavRoute.HOME_BOARDING
        ) {

            composable(route = NavRoute.HOME) { entry ->
                val viewModel =
                    entry.sharedViewModel<SharedViewModel>(navController = navController)

                HomeScreen(
                    onArticleClicked = {
                        viewModel.updateState(id = it)
                        navController.navigate(NavRoute.ARTICLE_INFO)
                    }
                )
            }

            composable(route = NavRoute.ARTICLE_INFO) { entry ->
                val viewModel =
                    entry.sharedViewModel<SharedViewModel>(navController = navController)
                val state by viewModel.sharedState.collectAsStateWithLifecycle()

                ArticleInfoScreen(
                    navController = navController,
                    id = state
                )
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}