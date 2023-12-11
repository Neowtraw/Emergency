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
import com.codingub.emergency.presentation.ui.customs.getBackgroundBrush
import com.codingub.emergency.presentation.ui.theme.EmergencyTheme
import com.codingub.emergency.presentation.ui.utils.Constants
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_PADDING

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        Modifier
            .background(getBackgroundBrush())
            .padding(
                top = 60.dp,
            )
            .padding(horizontal = MAIN_PADDING.dp)
    ) {

    }
}