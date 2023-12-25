package com.codingub.emergency.presentation.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codingub.emergency.R
import com.codingub.emergency.core.ArticleType
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.presentation.ui.customs.ArticleItem
import com.codingub.emergency.presentation.ui.customs.EmptyStateView
import com.codingub.emergency.presentation.ui.customs.ErrorStateView
import com.codingub.emergency.presentation.ui.customs.getBackgroundBrush
import com.codingub.emergency.presentation.ui.theme.monFamily
import com.codingub.emergency.presentation.ui.utils.Constants
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_ADDITIONAL_TEXT
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_CORNER
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_DIVIDER
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_PADDING
import com.codingub.emergency.presentation.ui.utils.shimmerEffect
import com.codingub.emergency.presentation.ui.viewmodels.ArticleViewModel
import com.codingub.emergency.presentation.ui.viewmodels.ArticlesState


@Composable
fun ArticleScreen(
    onArticleClicked: (String) -> Unit,
) {
    val articleViewModel: ArticleViewModel = hiltViewModel()


    var textValue: String by remember { mutableStateOf("") }
    val articlesScreenState by articleViewModel.articlesState.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxSize()
            .background(getBackgroundBrush())
            .statusBarsPadding()
            .padding(top = 40.dp)
            .padding(horizontal = MAIN_PADDING.dp)
    ) {

        Search(textValue = textValue, onTextChange = { text ->
            textValue = text
            articleViewModel.searchArticlesByAlt(alt = text)
        }, onIconClicked = {})

        ArticlesContent(states = articlesScreenState,
            onLikeClick = { id, liked ->
                articleViewModel.updateArticleToFavorite(id, liked)
            }, onCardClick = {
                onArticleClicked(it.id)
            }, onTabSelected = {
                articleViewModel.searchArticlesByAlt(alt = it)
            })
    }
}

@Composable
private fun ArticlesContent(
    states: ArticlesState,
    onLikeClick: (id: String, liked: Boolean) -> Unit,
    onCardClick: (Article) -> Unit,
    onTabSelected: (String) -> Unit
) {

    var isTabsVisible by rememberSaveable {
        mutableStateOf(false)
    }

    AnimatedVisibility(visible = isTabsVisible) {
        TabbedItem {
            onTabSelected(it)
        }
    }

    AnimatedContent(
        targetState = states, label = ""
    ) { state ->

        Column {
            when (state) {
                is ArticlesState.Loading -> {
                    isTabsVisible = false
                    ArticlesLoadingStateView()
                }

                is ArticlesState.Success -> {
                    isTabsVisible = true

                    ArticleGrid(
                        modifier = Modifier.weight(1f),
                        articles = state.articles,
                        onLikeClick = onLikeClick,
                        onCardClick = onCardClick
                    )
                }

                is ArticlesState.NotFound -> {
                    isTabsVisible = true
                    EmptyStateView(state.error)
                }

                is ArticlesState.Error -> {
                    isTabsVisible = true
                    ErrorStateView(message = state.error)

                }

                is ArticlesState.NetworkLost -> {
                    isTabsVisible = false
                    ErrorStateView(icon = R.drawable.ic_network_lost,
                        message = R.string.exception_network_lost)

                }
            }

        }
    }
}

@Composable
private fun ArticlesLoadingStateView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .height(50.dp)
                    .width(100.dp)
                    .shimmerEffect()
            )
        }

        Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            (0..3).forEach {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .padding(vertical = 5.dp)
                        .shimmerEffect()
                )
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ArticleGrid(
    articles: List<Article>,
    onLikeClick: (id: String, liked: Boolean) -> Unit,
    onCardClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = MAIN_PADDING.dp, bottom = 80.dp
        ),
        modifier = modifier
    ) {
        items(articles) { article ->
            Row(
                Modifier.animateItemPlacement(
                    tween(durationMillis = 500)
                )
            ) {
                ArticleItem(image = article.imageUrl,
                    title = article.title,
                    summary = article.summary,
                    onCardClick = { onCardClick(article) },
                    liked = article.liked,
                    onLikeClick = { onLikeClick(article.id, article.liked) })
            }
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Search(
    textValue: String, onTextChange: (String) -> Unit, onIconClicked: () -> Unit
) {
    Box(
        Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(50.dp)
    ) {

        val stroke = Stroke(
            width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
        val interactionSource = remember { MutableInteractionSource() }
        val contrastColor = colorResource(id = R.color.contrast)

        Canvas(
            Modifier.matchParentSize()
        ) {
            drawRoundRect(
                color = contrastColor,
                style = stroke,
                cornerRadius = CornerRadius(MAIN_CORNER.dp.toPx(), MAIN_CORNER.dp.toPx())
            )
        }

        Row(
            Modifier.matchParentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {

            OutlinedTextField(
                value = textValue,
                onValueChange = { onTextChange(it) },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search),
                        fontSize = MAIN_ADDITIONAL_TEXT.sp,
                        fontWeight = FontWeight.Light,
                        color = colorResource(id = R.color.add_text)
                    )
                },
                maxLines = 1,
                textStyle = TextStyle(color = colorResource(id = R.color.main_text)),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, autoCorrect = true
                ),
                visualTransformation = VisualTransformation.None,
                shape = RoundedCornerShape(MAIN_CORNER.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
                )
            )

            Icon(imageVector = if (textValue.isEmpty()) ImageVector.vectorResource(id = R.drawable.ic_search)
            else ImageVector.vectorResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = colorResource(id = R.color.contrast_icons),
                modifier = Modifier
                    .padding(MAIN_PADDING.dp)
                    //.size(20.dp)
                    .clickable(
                        interactionSource = interactionSource, indication = null
                    ) {
                        onIconClicked()
                    })
        }


    }
}

@Composable
private fun TabbedItem(
    modifier: Modifier = Modifier, onTabSelected: (String) -> Unit
) {
    val tabTitles = ArticleType.values()
    var selectedTabIndex by remember { mutableStateOf<Int?>(null) }

    Column(modifier) {
        TabRow(selectedTabIndex = selectedTabIndex ?: 0,
            backgroundColor = Color.Transparent,
            contentColor = colorResource(id = R.color.navbar_unselected),
            indicator = {}) {
            tabTitles.forEachIndexed { index, element ->
                val bgColor: Color by animateColorAsState(
                    if (selectedTabIndex == index) colorResource(id = R.color.tabrow_selected) else colorResource(
                        id = R.color.tabrow_unselected
                    ), label = "", animationSpec = tween(300, easing = LinearEasing)
                )

                Tab(
                    modifier = Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(bgColor),
                    text = {
                        Text(
                            text = stringResource(id = element.title),
                            maxLines = 1,
                            fontFamily = monFamily,
                            color = if (index == selectedTabIndex) colorResource(id = R.color.background_add)
                            else colorResource(id = R.color.main_text),
                            fontWeight = FontWeight.Medium,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 14.sp
                        )
                    },
                    selected = selectedTabIndex == index,
                    onClick = {

                        selectedTabIndex = index
                        onTabSelected(element.name)
                    },
                    selectedContentColor = colorResource(id = R.color.background_add),
                    unselectedContentColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
                )
            }
        }
    }
}

@Composable
fun TabIndicator(tabPosition: List<TabPosition>, index: Int) {
    val transition = updateTransition(targetState = index, label = "")
    val leftIndicator by transition.animateDp(label = "", transitionSpec = {
        spring(stiffness = Spring.StiffnessVeryLow)
    }) {
        tabPosition[it].left
    }

    val rightIndicator by transition.animateDp(label = "", transitionSpec = {
        spring(stiffness = Spring.StiffnessVeryLow)
    }) {
        tabPosition[it].right
    }

    Box(
        Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = leftIndicator)
            .width(rightIndicator - leftIndicator)
            .padding(4.dp)
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))

    )
}