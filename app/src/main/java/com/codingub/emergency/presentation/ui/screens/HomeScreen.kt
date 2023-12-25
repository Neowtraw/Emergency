package com.codingub.emergency.presentation.ui.screens


import android.Manifest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codingub.emergency.R
import com.codingub.emergency.presentation.ui.customs.ActionBar
import com.codingub.emergency.presentation.ui.customs.ContractedArticleItem
import com.codingub.emergency.presentation.ui.customs.InfoHeaderText
import com.codingub.emergency.presentation.ui.customs.MemoView
import com.codingub.emergency.presentation.ui.customs.getBackgroundBrush
import com.codingub.emergency.presentation.ui.theme.monFamily
import com.codingub.emergency.presentation.ui.utils.Constants
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_PADDING
import com.codingub.emergency.presentation.ui.viewmodels.HomeViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.collectLatest


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    onArticleClicked: (String) -> Unit,
    onBoxClicked: () -> Unit,
) {
    val homeViewModel: HomeViewModel = hiltViewModel()

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(getBackgroundBrush())
            .padding(top = 40.dp, bottom = 60.dp)
    ) {

        val articles by homeViewModel.articles.collectAsState()
        val interactionSource = remember { MutableInteractionSource() }
        var columnHeight by remember {
            Log.d("articles", articles.size.toString())
            mutableStateOf(100.dp)
        }
        val animatedHeight by animateDpAsState(
            targetValue = columnHeight,
            label = "",
            animationSpec = tween(durationMillis = 500)
        )
        val notificationPermissionState =
            rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)


        DisposableEffect(notificationPermissionState) {
            if (notificationPermissionState.status.isGranted) {
                homeViewModel.initializeWorkManager()
            } else {
                notificationPermissionState.launchPermissionRequest()
            }
            onDispose {}
        }

        LaunchedEffect(LocalContext.current) {
            homeViewModel.articles.collectLatest {
                if (articles.isEmpty()) {
                    columnHeight = 100.dp
                    return@collectLatest
                }
                columnHeight = (it.size * 100).dp
            }
        }


        ActionBar(text = R.string.title_own_data)
        Spacer(modifier = Modifier.height(30.dp))

        InfoHeaderText(
            text = "Избранное", modifier = Modifier.padding(horizontal = MAIN_PADDING.dp)
        )
        Spacer(modifier = Modifier.height(MAIN_PADDING.dp))

        if (articles.isEmpty()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MAIN_PADDING.dp)
                    .height(animatedHeight)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onBoxClicked()
                    }) {

                val stroke = Stroke(
                    width = 3f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )
                val warnColor = colorResource(id = R.color.warn_text)

                Canvas(
                    Modifier
                        .matchParentSize()
                ) {
                    drawRoundRect(
                        color = warnColor,
                        style = stroke,
                        cornerRadius = CornerRadius(
                            Constants.MAIN_CORNER.dp.toPx(),
                            Constants.MAIN_CORNER.dp.toPx()
                        )
                    )
                }

                Text(
                    text = "Нет избранных статей",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    fontWeight = FontWeight.Light,
                    color = colorResource(id = R.color.warn_text),
                    textAlign = TextAlign.Center,
                    fontFamily = monFamily,
                    fontSize = Constants.MAIN_ADDITIONAL_TEXT.sp
                )
            }
        } else {
            LazyColumn(
                Modifier
                    .height(animatedHeight)
                    .padding(horizontal = MAIN_PADDING.dp)
            ) {
                items(articles, key = { it.id }) { article ->
                    Row(
                        Modifier.animateItemPlacement(
                            tween(durationMillis = 500)
                        )
                    ) {
                        ContractedArticleItem(
                            image = article.imageUrl,
                            title = article.title,
                            liked = article.liked,
                            onLikeClick = {
                                homeViewModel.updateFavoriteArticles(
                                    article.id,
                                    article.liked
                                )
                            },
                            onCardClick = {
                                onArticleClicked(article.id)
                            })

                    }
                }
            }
        }

        Column(Modifier.padding(horizontal = MAIN_PADDING.dp)) {

            Spacer(modifier = Modifier.height(MAIN_PADDING.dp))
            MemoView(
                headerText = R.string.title_algorithm_help,
                contextText = R.string.algorithm_help
            )
            MemoView(
                headerText = R.string.title_conscious_victim,
                contextText = R.string.conscious_victim
            )
            MemoView(
                headerText = R.string.title_unconscious_victim,
                contextText = R.string.unconscious_victim
            )
        }
    }
}
