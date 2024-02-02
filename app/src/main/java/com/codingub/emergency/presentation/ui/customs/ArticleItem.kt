package com.codingub.emergency.presentation.ui.customs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.codingub.emergency.presentation.ui.theme.monFamily
import com.codingub.emergency.presentation.ui.utils.Constants
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleItem(
    image: String,
    title: String,
    summary: String,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(vertical = 5.dp),
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
                .aspectRatio(667f / 375f)
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
        
        Column(Modifier.background(colorResource(id = R.color.background))
            .padding(MAIN_PADDING.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = Constants.MAIN_CONTENT_TEXT.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = monFamily,
                        color = colorResource(id = R.color.contrast),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            Text(
                text = summary,
                style = TextStyle(
                    fontSize = Constants.MAIN_ADDITIONAL_TEXT.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = monFamily,
                    color = colorResource(id = R.color.main_text),
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 3
            )
        }
    }
}

@Composable
fun FavoriteIcon(
    liked: Boolean,
    onLikeClick: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    val animatedColor by animateColorAsState(
        targetValue = if (liked) colorResource(id = R.color.article_favorite_view_selected)
        else colorResource(id = R.color.article_favorite_view_unselected),
        label = "favorite",
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
    )

    // Иконка (звездочка)
    Icon(
        imageVector = if (liked) ImageVector.vectorResource(id = R.drawable.ic_favorite_selected)
        else ImageVector.vectorResource(id = R.drawable.ic_favorite),
        contentDescription = null,
        tint = animatedColor,
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onLikeClick()
            }
            .size(25.dp)

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractedArticleItem(
    image: String,
    title: String,
    liked: Boolean,
    onLikeClick: () -> Unit,
    onCardClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 5.dp),
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(brush = getImageBrush())
        ) {
            AsyncImage(
                model = image,
                contentDescription = "Article View",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillWidth,
                error = painterResource(R.drawable.placeholder)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = getImageBrush())
            )

            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .padding(Constants.MAIN_PADDING.dp)
                    .align(Alignment.BottomCenter)
            ) {

                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = Constants.MAIN_CONTENT_TEXT.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = monFamily,
                        color = colorResource(id = R.color.contrast),
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp)
                )

                FavoriteIcon(liked = liked) {
                    onLikeClick()
                }

            }

        }

    }

}