package com.codingub.emergency.presentation.ui.screens

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import coil.compose.AsyncImage
import com.codingub.emergency.R
import com.codingub.emergency.common.ArticleType
import com.codingub.emergency.common.ResultState
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.presentation.ui.customs.getBackgroundBrush
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_ADDITIONAL_TEXT
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_CONTENT_TEXT
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_CORNER
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_DIVIDER_ITEMS
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_ELEVATION
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_PADDING
import com.codingub.emergency.presentation.ui.utils.ScreenState
import com.codingub.emergency.presentation.ui.viewmodels.ArticleViewModel
import kotlinx.coroutines.flow.collectLatest


@Composable
fun ArticleScreen(
    onArticleClicked: (Article) -> Unit,
    articleViewModel: ArticleViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    var textValue: String by remember { mutableStateOf("") }
    var screenState by remember { mutableStateOf<ScreenState<List<Article>>>(ScreenState.Loading) }


    LaunchedEffect(articleViewModel, context) {
        articleViewModel.articles.collectLatest { result ->
            when (result) {
                is ResultState.Loading -> screenState = ScreenState.Loading
                is ResultState.Success -> screenState = ScreenState.Success(data = result.data!!)
                is ResultState.Error -> screenState =
                    ScreenState.Error(error = result.error ?: Throwable())
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(getBackgroundBrush())
            .statusBarsPadding()
            .padding(top = 40.dp)
            .padding(horizontal = MAIN_PADDING.dp)
    ) {

        Search(textValue = textValue,
            onTextChange = { text ->
                textValue = text
                articleViewModel.searchArticlesByAlt(alt = text)
            }, onIconClicked = {})
        Spacer(modifier = Modifier.height(20.dp))
        TabbedItem {
            articleViewModel.searchArticlesByAlt(alt = it)
        }

        when (screenState) {
            ScreenState.Loading -> {}
            is ScreenState.Success -> {
                ArticleGrid(
                    articles = articleViewModel.articles.collectAsState().value.data!!,
                    onLikeClick = {},
                    onCardClick = {
                        onArticleClicked(it)
                    })
            }

            is ScreenState.Error -> {}
        }
    }
}


@Composable
private fun ArticleGrid(
    articles: List<Article>,
    onLikeClick: () -> Unit,
    onCardClick: (Article) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = MAIN_PADDING.dp,
            bottom = 80.dp
        )
    ) {
        items(articles) { article ->
            ArticleItem(
                image = article.imageUrl,
                title = article.title,
                summary = article.summary,
                onCardClick = { onCardClick(article) },
                onLikeClick = { onLikeClick() })
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArticleItem(
    image: String,
    title: String,
    summary: String,
    //liked: Boolean,
    onLikeClick: () -> Unit,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp)
            .height(280.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = MAIN_ELEVATION.dp
        ),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            contentColor = colorResource(id = R.color.article_view_content),
            containerColor = colorResource(id = R.color.background)
        ),
        onClick = onCardClick
    ) {

        AsyncImage(
            model = image,
            contentDescription = "Article View",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .aspectRatio(1280f / 847f)
                .clip(
                    shape = RoundedCornerShape(
                        MAIN_CORNER.dp,
                        MAIN_CORNER.dp,
                        0.dp,
                        0.dp
                    )
                ),
            contentScale = ContentScale.FillWidth,
            error = painterResource(R.drawable.placeholder)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(Color.White)

        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(MAIN_PADDING.dp, MAIN_PADDING.dp, 0.dp, MAIN_PADDING.dp)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = MAIN_CONTENT_TEXT.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.main_text),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(MAIN_DIVIDER_ITEMS.dp))

                Text(
                    text = summary,
                    style = TextStyle(
                        fontSize = MAIN_ADDITIONAL_TEXT.sp,
                        fontWeight = FontWeight.Normal,
                        color = colorResource(id = R.color.main_text),
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )
            }

            // Иконка (звездочка)
            Icon(
                imageVector = if (true) ImageVector.vectorResource(id = R.drawable.ic_favorite)
                else ImageVector.vectorResource(id = R.drawable.ic_favorite),
                contentDescription = null,
                tint = if (true) colorResource(id = R.color.article_favorite_view_selected)
                else colorResource(id = R.color.article_favorite_view_unselected),
                modifier = Modifier
                    .clickable { onLikeClick() }
                    .size(25.dp)
                    .padding(horizontal = MAIN_PADDING.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Search(
    textValue: String,
    onTextChange: (String) -> Unit,
    onIconClicked: () -> Unit
) {
    Box(
        Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(50.dp)
    ) {

        val stroke = Stroke(
            width = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
        val contrastColor = colorResource(id = R.color.contrast)

        Canvas(
            Modifier
                .matchParentSize()
        ) {
            drawRoundRect(
                color = contrastColor,
                style = stroke,
                cornerRadius = CornerRadius(MAIN_CORNER.dp.toPx(), MAIN_CORNER.dp.toPx())
            )
        }

        Row(
            Modifier
                .matchParentSize(),
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
                    keyboardType = KeyboardType.Text,
                    autoCorrect = true
                ),
                visualTransformation = VisualTransformation.None,
                shape = RoundedCornerShape(MAIN_CORNER.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Icon(
                imageVector = if (textValue.isEmpty()) ImageVector.vectorResource(id = R.drawable.ic_search)
                else ImageVector.vectorResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = colorResource(id = R.color.contrast_icons),
                modifier = Modifier
                    .padding(MAIN_PADDING.dp)
                    .size(20.dp)
                    .clickable {
                        onIconClicked()
                    }
            )
        }


    }
}

@Composable
private fun TabbedItem(
    modifier: Modifier = Modifier,
    onTabSelected: (String) -> Unit
) {
    val tabTitles = ArticleType.values()
    var selectedTabIndex by remember { mutableStateOf<Int?>(null) }

    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = selectedTabIndex ?: 0,
            backgroundColor = Color.Transparent,
            contentColor = colorResource(id = R.color.navbar_unselected),
            indicator = {}
        ) {
            tabTitles.forEachIndexed { index, element ->
                val bgColor: Color by animateColorAsState(
                    if (selectedTabIndex == index) colorResource(id = R.color.tabrow_selected) else colorResource(
                        id = R.color.tabrow_unselected
                    ),
                    label = "",
                    animationSpec = tween(300, easing = LinearEasing)
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
                            color = if(index == selectedTabIndex) colorResource(id = R.color.background_add)
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
fun TabIndicator(tabPosition: List<TabPosition>, index: Int){
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