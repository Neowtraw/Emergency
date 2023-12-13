package com.codingub.emergency.presentation.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.codingub.emergency.presentation.ui.customs.ArticleItem
import com.codingub.emergency.presentation.ui.customs.getBackgroundBrush
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_PADDING
import com.codingub.emergency.presentation.ui.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(getBackgroundBrush())
            .padding(top = 60.dp)
            .padding(horizontal = MAIN_PADDING.dp)
    ) {

        val articles by homeViewModel.articles.collectAsState()

        Text(text = "Favorite Articles")

        LazyColumn(){
            items(articles) { article ->
                ArticleItem(
                    image = article.imageUrl,
                    title = article.title,
                    summary = article.summary,
                    liked = article.liked,
                    onLikeClick = {  }) {
                    
                }
            }
        }

    }
}