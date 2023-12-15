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
import com.codingub.emergency.presentation.navigation.NavRoute.ARTICLES
import com.codingub.emergency.presentation.navigation.NavRoute.ARTICLE_BOARDING
import com.codingub.emergency.presentation.navigation.NavRoute.ARTICLE_INFO
import com.codingub.emergency.presentation.navigation.NavRoute.ARTICLE_INFO_I
import com.codingub.emergency.presentation.navigation.NavRoute.AUTH_BOARDING
import com.codingub.emergency.presentation.navigation.NavRoute.HOME
import com.codingub.emergency.presentation.navigation.NavRoute.HOME_BOARDING
import com.codingub.emergency.presentation.navigation.NavRoute.INFO
import com.codingub.emergency.presentation.navigation.NavRoute.USER_AUTH
import com.codingub.emergency.presentation.navigation.NavRoute.USER_INFO
import com.codingub.emergency.presentation.navigation.NavRoute.USER_VERIFICATION
import com.codingub.emergency.presentation.navigation.NavRoute.WELCOME
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
        composable(route = INFO) {
            InfoScreen()
        }

        composable(route = WELCOME) {
            WelcomeScreen(onFinishButtonClicked = {
                navController.popBackStack()
                navController.navigate(HOME)
            })

        }

        navigation(
            startDestination = USER_AUTH,
            route = AUTH_BOARDING
        ) {
            composable(route = USER_AUTH) {
                UserAuthScreen(
                    activity = activity,
                    onAuthFinished = {
                        navController.navigate(USER_VERIFICATION)
                    }
                )
            }

            composable(route = USER_VERIFICATION) {
                UserVerificationScreen(
                    onVerificationFinished = {
                        navController.navigate(USER_INFO)
                    }
                )
            }

            composable(route = USER_INFO) {
                UserInfoScreen(
                    onInfoRecorded = {
                        navController.navigate(HOME_BOARDING) {
                            popUpTo(AUTH_BOARDING) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }

        navigation(
            startDestination = HOME,
            route = HOME_BOARDING
        ) {

            composable(route = HOME) { entry ->
                val viewModel =
                    entry.sharedViewModel<SharedViewModel>(navController = navController)

                HomeScreen(
                    onArticleClicked = {
                        viewModel.updateState(id = it)
                        navController.navigate(ARTICLE_INFO)
                    }
                )
            }

            composable(route = ARTICLE_INFO) { entry ->
                val viewModel =
                    entry.sharedViewModel<SharedViewModel>(navController = navController)
                val state by viewModel.sharedState.collectAsStateWithLifecycle()

                ArticleInfoScreen(
                    id = state,
                    onBackClicked = {
                        navController.popBackStack()
                    }
                )
            }
        }

        navigation(
            startDestination = ARTICLES,
            route = ARTICLE_BOARDING
        ) {

            composable(route = ARTICLE_INFO_I) { entry ->
                val viewModel =
                    entry.sharedViewModel<SharedViewModel>(navController = navController)
                val state by viewModel.sharedState.collectAsStateWithLifecycle()

                ArticleInfoScreen(
                    id = state,
                    onBackClicked = {
                        navController.popBackStack()
                    }
                )
            }

            composable(route = ARTICLES) { entry ->
                val viewModel =
                    entry.sharedViewModel<SharedViewModel>(navController = navController)

                ArticleScreen(
                    onArticleClicked = {
                        viewModel.updateState(id = it)
                        navController.navigate(ARTICLE_INFO_I)
                    }
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