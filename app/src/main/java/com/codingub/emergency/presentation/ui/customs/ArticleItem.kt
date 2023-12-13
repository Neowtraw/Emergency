package com.codingub.emergency.presentation.ui.customs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codingub.emergency.R
import com.codingub.emergency.presentation.ui.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleItem(
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
            .padding(top = 5.dp, bottom = 5.dp)
            .height(280.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Constants.MAIN_ELEVATION.dp
        ),
        shape = RoundedCornerShape(Constants.MAIN_CORNER.dp),
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
                        Constants.MAIN_CORNER.dp,
                        Constants.MAIN_CORNER.dp,
                        0.dp,
                        0.dp
                    )
                ),
            contentScale = ContentScale.FillWidth,
            error = painterResource(R.drawable.placeholder)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.White)
                .padding(end = Constants.MAIN_PADDING.dp)


        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(Constants.MAIN_PADDING.dp, Constants.MAIN_PADDING.dp, 0.dp, Constants.MAIN_PADDING.dp)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = Constants.MAIN_CONTENT_TEXT.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.main_text),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(Constants.MAIN_DIVIDER_ITEMS.dp))

                Text(
                    text = summary,
                    style = TextStyle(
                        fontSize = Constants.MAIN_ADDITIONAL_TEXT.sp,
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
                imageVector = if (liked) ImageVector.vectorResource(id = R.drawable.ic_favorite)
                else ImageVector.vectorResource(id = R.drawable.ic_favorite),
                contentDescription = null,
                tint = if (liked) colorResource(id = R.color.article_favorite_view_selected)
                else Color.Red,
                modifier = Modifier
                    .clickable { onLikeClick() }
                    .size(25.dp)

            )
        }
    }
}