package com.codingub.emergency.presentation.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.codingub.emergency.R
import com.codingub.emergency.presentation.ui.customs.FavoriteIcon
import com.codingub.emergency.presentation.ui.customs.getBackgroundBrush
import com.codingub.emergency.presentation.ui.customs.getImageBrush
import com.codingub.emergency.presentation.ui.theme.monFamily
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_ADDITIONAL_TEXT
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_CONTENT_TEXT
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_DIVIDER
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_HEADER_TEXT
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_PADDING
import com.codingub.emergency.presentation.ui.viewmodels.ArticleInfoViewModel

@Composable
fun ArticleInfoScreen(
    id: String,
    onBackClicked: () -> Unit
) {
    val articleInfoViewModel: ArticleInfoViewModel = hiltViewModel()

    articleInfoViewModel.getSavedArticle(id)
    val article by articleInfoViewModel.article.collectAsState()

    val interactionSource = remember { MutableInteractionSource() }

    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    BackHandler {
        println("back pressed")
        onBackClicked()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(getBackgroundBrush())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .height(300.dp)
                .background(brush = getImageBrush())
        ) {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = "Article View",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillHeight,
                error = painterResource(R.drawable.placeholder)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = getImageBrush())
            )

        }

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
            contentDescription = "Back",
            tint = colorResource(id = R.color.main_text),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(MAIN_PADDING.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onBackClicked()
                }
                .size(30.dp)
        )


        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 200.dp, bottom = 50.dp)
                .padding(horizontal = MAIN_PADDING.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = article.title, fontSize = MAIN_HEADER_TEXT.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    fontFamily = monFamily,
                    color = colorResource(id = R.color.main_text),
                    modifier = Modifier.weight(1f)
                )

                FavoriteIcon(liked = article.liked) {
                    articleInfoViewModel.updateArticleToFavorite(article.id, article.liked)
                }
            }

            Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))
            Text(
                text = article.summary, fontSize = MAIN_ADDITIONAL_TEXT.sp,
                fontWeight = FontWeight.Light,
                fontFamily = monFamily,
                textAlign = TextAlign.Start,
                color = colorResource(id = R.color.add_text)
            )
            Spacer(modifier = Modifier.height(60.dp))
            article.content.forEach { content ->
                if (!content.title.isNullOrEmpty()) {
                    Text(
                        text = content.title,
                        fontSize = MAIN_CONTENT_TEXT.sp,
                        fontFamily = monFamily,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        color = colorResource(id = R.color.main_text)
                    )
                }
                Text(
                    text = content.description,
                    fontSize = MAIN_CONTENT_TEXT.sp,
                    fontFamily = monFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    color = colorResource(id = R.color.main_text)
                )
                if (content.imageUrl.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeightIn(max = 300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = content.description,
                            contentDescription = "Article View",
                            contentScale = ContentScale.Fit,
                            error = painterResource(R.drawable.placeholder)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))
            AndroidView(
                factory = { context ->
                    PlayerView(context).also {
                        it.player = articleInfoViewModel.player
                    }
                },
                update = {
                    when (lifecycle) {
                        Lifecycle.Event.ON_PAUSE -> {
                            it.onPause()
                            it.player?.pause()
                        }

                        Lifecycle.Event.ON_RESUME -> {
                            it.onResume()
                        }

                        else -> Unit
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}