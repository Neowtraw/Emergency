package com.codingub.emergency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codingub.emergency.common.NavigationRoute.ARTICLES
import com.codingub.emergency.common.NavigationRoute.HOME
import com.codingub.emergency.common.NavigationRoute.INFO
import com.codingub.emergency.ui.screens.ArticleScreen
import com.codingub.emergency.ui.screens.MainScreen
import com.codingub.emergency.ui.theme.EmergencyTheme
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable
import dagger.hilt.android.AndroidEntryPoint


@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmergencyTheme(dynamicColor = false) {
                val navController = rememberNavController()

                val navigationBarItems = remember { NavigationBarItems.values() }
                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {

                    Scaffold(
                        bottomBar = {
                            AnimatedNavigationBar(
                                modifier = Modifier.height(64.dp),
                                selectedIndex = selectedItemIndex,
                                cornerRadius = shapeCornerRadius(cornerRadius = 34.dp),
                                ballAnimation = Parabolic(tween(300)),
                                indentAnimation = Height(tween(300)),
                                barColor = colorResource(id = R.color.navbar_color),
                                ballColor = colorResource(id = R.color.navball_color)
                            ) {
                                navigationBarItems.forEach { item ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .noRippleClickable {
                                                selectedItemIndex = item.ordinal
                                                navController.navigate(item.route)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(26.dp),
                                            imageVector = ImageVector.vectorResource(item.icon),
                                            contentDescription = "Bottom Bar Icon",
                                            tint = if (selectedItemIndex == item.ordinal) MaterialTheme.colorScheme.secondary
                                            else MaterialTheme.colorScheme.inversePrimary
                                        )
                                    }
                                }
                            }
                        },
                        content = { padding ->
                            NavHost(
                                navController = navController,
                                startDestination = ARTICLES,
                                modifier = Modifier.padding(padding)
                            ) {
                                composable(route = ARTICLES) {
                                    ArticleScreen(
                                        navController = navController
                                    )
                                }
                                composable(route = HOME) {
                                    MainScreen(
                                        navController = navController
                                    )
                                }
                                composable(route = INFO) {
                                    ArticleScreen(
                                        navController = navController
                                    )
                                }

                            }
                        }
                    )
                }
            }
        }
    }
}


enum class NavigationBarItems(val route: String, val icon: Int) {
    Home(route = HOME, icon = R.drawable.ic_home),
    Articles(route = ARTICLES, icon = R.drawable.ic_articles),
    Info(route = INFO, icon = R.drawable.ic_info)
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember {
            MutableInteractionSource()
        }) {
        onClick()
    }
}
