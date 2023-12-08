package com.codingub.emergency

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codingub.emergency.presentation.navigation.NavRoute.ARTICLES
import com.codingub.emergency.presentation.navigation.NavRoute.HOME
import com.codingub.emergency.presentation.navigation.NavRoute.INFO
import com.codingub.emergency.presentation.navigation.NavRoute.USER_AUTH
import com.codingub.emergency.presentation.navigation.NavRoute.WELCOME
import com.codingub.emergency.presentation.navigation.setupNavGraph
import com.codingub.emergency.presentation.ui.theme.EmergencyTheme
import com.codingub.emergency.presentation.ui.viewmodels.SplashViewModel
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        installSplashScreen().setKeepOnScreenCondition {
            splashViewModel.isLoading.value
        }

        setContent {
            EmergencyTheme(dynamicColor = false) {
                val screen by splashViewModel.startDestination
                val navController = rememberNavController()
                val navigationBarItems = remember { NavigationBarItems.values() }
                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }
                val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                when(navBackStackEntry?.destination?.route) {
                    WELCOME, USER_AUTH -> bottomBarState.value = false
                    ARTICLES, HOME, INFO -> bottomBarState.value = true
                }


                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Scaffold(
                        bottomBar = {
                            AnimatedVisibility(visible = bottomBarState.value,
                                enter = slideInVertically(initialOffsetY = {it}),
                                exit = slideOutVertically(targetOffsetY = {it}),
                                content = {

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
                                }
                            )
                        },
                        content = { padding ->
                            HideSystemBars()

                            setupNavGraph(
                                navController = navController,
                                startDestination = screen,
                                padding = padding,
                                activity = this
                            )
                        }
                    )
                }
            }
        }
    }
}

enum class NavigationBarItems(val route: String, val icon: Int) {
    Home(route = USER_AUTH, icon = R.drawable.ic_home),
    Articles(route = ARTICLES, icon = R.drawable.ic_articles),
    Info(route = INFO, icon = R.drawable.ic_info)
}

@Composable
fun HideSystemBars() {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val window = context.findActivity()?.window ?: return@DisposableEffect onDispose {}
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        insetsController.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            insetsController.apply {
                show(WindowInsetsCompat.Type.statusBars())
                show(WindowInsetsCompat.Type.navigationBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
            }
        }
    }
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
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
