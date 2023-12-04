package com.codingub.emergency

import android.graphics.drawable.Drawable
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
import androidx.compose.runtime.Composable
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
import com.codingub.emergency.ui.screens.MainScreen
import com.codingub.emergency.ui.theme.EmergencyTheme
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable


data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val badge: Boolean = false
)


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmergencyTheme(dynamicColor = false) {
                val items = listOf(
                    BottomNavigationItem(
                        title = "Home",
                        icon = ImageVector.vectorResource(id = R.drawable.ic_home)
                    ),
                    BottomNavigationItem(
                        title = "Articles",
                        icon = ImageVector.vectorResource(id = R.drawable.ic_articles),
                        badge = true
                    ),
                    BottomNavigationItem(
                        title = "Info",
                        icon = ImageVector.vectorResource(id = R.drawable.ic_info)
                    ),
                )

                val navigationBarItems = remember { NavigationBarItems.values() }
                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
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
                                    Box(modifier = Modifier
                                        .fillMaxSize()
                                        .noRippleClickable { selectedItemIndex = item.ordinal },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(26.dp),
                                            imageVector = ImageVector.vectorResource(item.icon),
                                            contentDescription = "Bottom Bar Icon",
                                            tint = if(selectedItemIndex == item.ordinal) MaterialTheme.colorScheme.secondary
                                            else MaterialTheme.colorScheme.inversePrimary
                                        )
                                    }
                                }
                            }
                        },
                        content = { padding ->
                            Column(modifier = Modifier.padding(padding)) {}
                        }
                    )
                }
            }
        }
    }
}


enum class NavigationBarItems(val icon: Int) {
    Home(icon = R.drawable.ic_home),
    Articles(icon = R.drawable.ic_articles),
    Info(icon = R.drawable.ic_info)
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
