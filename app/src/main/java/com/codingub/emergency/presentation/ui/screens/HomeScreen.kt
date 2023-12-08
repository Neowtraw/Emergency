package com.codingub.emergency.presentation.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codingub.emergency.R
import com.codingub.emergency.presentation.ui.theme.EmergencyTheme
import com.codingub.emergency.presentation.ui.utils.Constants

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        Modifier
            .padding(
                Constants.MAIN_PADDING.dp, 60.dp,
                Constants.MAIN_PADDING.dp,
                Constants.MAIN_PADDING.dp
            )
    ) {
        Text(text = "heeee\nee\nll")

    }
}


@Composable
@Preview(device = "id:pixel_4a", showBackground = true, backgroundColor = 0xFF3A2F6E)
private fun MainScreenPreview() {
    EmergencyTheme {
    }
}