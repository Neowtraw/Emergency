package com.codingub.emergency.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingub.emergency.R
import com.codingub.emergency.common.Constants.MAIN_CONTENT_TEXT
import com.codingub.emergency.common.Constants.MAIN_DIVIDER
import com.codingub.emergency.common.Constants.MAIN_ELEVATION
import com.codingub.emergency.common.Constants.MAIN_HEADER_TEXT
import com.codingub.emergency.common.Constants.MAIN_PADDING
import com.codingub.emergency.ui.theme.EmergencyTheme

@Composable
fun ArticleScreen() {


}

@Composable
private fun createSearch(){

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun createArticle(
    image: Painter,
    title: String,
    summary: String,
    liked: Boolean,
    onLikeClick: () -> Unit,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            // .padding(MAIN_PADDING.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(MAIN_ELEVATION.dp)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = MAIN_ELEVATION.dp
        ),
        colors = CardDefaults.cardColors(
            contentColor = colorResource(id = R.color.article_view_content)
        ),
        border = BorderStroke(1.dp, colorResource(id = R.color.article_view_stroke)),
        onClick = onCardClick
    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
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
            // Колонка с текстом
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(MAIN_PADDING.dp,MAIN_PADDING.dp,0.dp,MAIN_PADDING.dp)
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
            )
        }
    }
}


@Composable
@Preview(device = "id:pixel_4a", showBackground = true, backgroundColor = 0xFF3A2F6E)
private fun MainScreenPreview() {
    EmergencyTheme {
        createArticle(painterResource(id = R.drawable.placeholder), "title", "summary", true, {}, {})
    }
}

