package com.codingub.emergency.presentation.ui.screens


import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.codingub.emergency.R
import com.codingub.emergency.presentation.navigation.NavRoute.ARTICLE_INFO
import com.codingub.emergency.presentation.ui.customs.ActionBar
import com.codingub.emergency.presentation.ui.customs.ContractedArticleItem
import com.codingub.emergency.presentation.ui.customs.InfoHeaderText
import com.codingub.emergency.presentation.ui.customs.getBackgroundBrush
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_PADDING
import com.codingub.emergency.presentation.ui.viewmodels.HomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onArticleClicked: (String) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    Column(
        Modifier
            .fillMaxSize()
            .background(getBackgroundBrush())
            .padding(top = 40.dp)
    ) {

        val articles by homeViewModel.articles.collectAsState()

        ActionBar(text = R.string.title_own_data)
        Spacer(modifier = Modifier.height(30.dp))

        InfoHeaderText(
            text = "Избранное", modifier = Modifier.padding(horizontal = MAIN_PADDING.dp)
        )
        Spacer(modifier = Modifier.height(MAIN_PADDING.dp))

        LazyColumn(
            Modifier.padding(horizontal = MAIN_PADDING.dp)
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
}