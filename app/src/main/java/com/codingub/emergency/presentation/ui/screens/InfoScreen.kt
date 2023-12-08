package com.codingub.emergency.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codingub.emergency.R
import com.codingub.emergency.domain.models.Call
import com.codingub.emergency.presentation.ui.theme.EmergencyTheme
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_ELEVATION

@Composable
fun InfoScreen(navController: NavController) {

    Column {
        Text(text = stringResource(id = R.string.emergency_service))


    }
}

@Composable
private fun PhoneGrid(){
    val phones = Call.values()

    LazyVerticalGrid(
        columns = GridCells.Fixed(1)
    ) {
        items(phones) { phone ->
            PhoneItem(
                title = stringResource(id = phone.title),
                onIconClicked = {}
            )
            }
        }
}

@Composable
private fun PhoneItem(
    title: String,
    onIconClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = colorResource(id = R.color.main_text))
        Box(
            modifier = Modifier
                .size(40.dp)
                .shadow(MAIN_ELEVATION.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colorResource(id = R.color.call))
                .clickable { onIconClicked() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_call),
                tint = colorResource(id = R.color.call_icon),
                contentDescription = "Call",
                modifier = Modifier.size(24.dp)
            )
        }

    }
}

@Composable
@Preview(device = "id:pixel_4a", showBackground = true)
private fun InfoScreenPreview() {

    EmergencyTheme {
        PhoneGrid()
    }
}


