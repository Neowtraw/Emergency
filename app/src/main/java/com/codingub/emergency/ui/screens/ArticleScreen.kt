package com.codingub.emergency.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.codingub.emergency.R
import com.codingub.emergency.common.Constants.MAIN_CONTENT_TEXT
import com.codingub.emergency.common.Constants.MAIN_CORNER
import com.codingub.emergency.common.Constants.MAIN_DIVIDER
import com.codingub.emergency.common.Constants.MAIN_ELEVATION
import com.codingub.emergency.common.Constants.MAIN_HEADER_TEXT
import com.codingub.emergency.common.Constants.MAIN_PADDING
import com.codingub.emergency.domain.models.Article
import com.codingub.emergency.ui.theme.EmergencyTheme


@Composable
fun ArticleScreen(navController: NavController) {

    var textValue : String by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(MAIN_PADDING.dp)
            .statusBarsPadding()
            .background(colorResource(id = R.color.background))
    ) {

        Search(textValue = textValue,
            onTextChange = { text ->
                textValue = text
            }, onIconClicked = {})
        Spacer(modifier = Modifier.height(20.dp))

        ArticleGrid(articles = listOf(Article(), Article(), Article(), Article()))
    }
}


@Composable
private fun ArticleGrid(articles: List<Article>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        items(articles) { article ->
            ArticleItem(
                image = "https://www.conic.org.br/portal/images/dim_2023_d.jpg",
                title = article.title,
                summary = article.summary,
                liked = article.liked,
                onLikeClick = { /*TODO*/ }) {

            }
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
            .padding(MAIN_PADDING.dp)
            .fillMaxWidth()
            .height(50.dp)
    ) {

        val stroke = Stroke(
            width = 2f,
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
                placeholder = { Text("Search") },
                maxLines = 1,
                textStyle = TextStyle(color = colorResource(id = R.color.main_text)),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    autoCorrect = true
                ),
                visualTransformation = VisualTransformation.None,
                shape = RoundedCornerShape(15.dp),
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
                    .clickable { }
                    .padding(MAIN_PADDING.dp)
                    .size(20.dp)
                    .clickable {
                        onIconClicked()
                    }
            )
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArticleItem(
    image: String,
    title: String,
    summary: String,
    liked: Boolean,
    onLikeClick: () -> Unit,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(MAIN_ELEVATION.dp))
            .padding(3.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = MAIN_ELEVATION.dp
        ),
        colors = CardDefaults.cardColors(
            contentColor = colorResource(id = R.color.article_view_content),
            containerColor = colorResource(id = R.color.background)
        ),
        border = BorderStroke(1.dp, colorResource(id = R.color.article_view_stroke)),
        onClick = onCardClick
    ) {
        AsyncImage(
            model = image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .aspectRatio(1280f / 847f)
                .clip(
                    shape = RoundedCornerShape(
                        MAIN_ELEVATION.dp,
                        MAIN_ELEVATION.dp,
                        0.dp,
                        0.dp
                    )
                ),
            contentScale = ContentScale.FillWidth
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(MAIN_PADDING.dp, MAIN_PADDING.dp, 0.dp, MAIN_PADDING.dp)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = MAIN_HEADER_TEXT.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.main_text),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))

                Text(
                    text = summary,
                    style = TextStyle(
                        fontSize = MAIN_CONTENT_TEXT.sp,
                        fontWeight = FontWeight.Normal,
                        color = colorResource(id = R.color.main_text),
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Иконка (звездочка)
            Icon(
                imageVector = if (liked) ImageVector.vectorResource(id = R.drawable.ic_favorite)
                else ImageVector.vectorResource(id = R.drawable.ic_favorite),
                contentDescription = null,
                tint = if (liked) colorResource(id = R.color.article_favorite_view_selected)
                else colorResource(id = R.color.article_favorite_view_unselected),
                modifier = Modifier
                    .clickable { onLikeClick() }
                    .padding(MAIN_PADDING.dp)
                    .size(20.dp)
            )
        }
    }
}


@Composable
@Preview(device = "id:pixel_4a", showBackground = true)
private fun MainScreenPreview() {

    EmergencyTheme {
        ArticleScreen(navController = rememberNavController())
    }
}

