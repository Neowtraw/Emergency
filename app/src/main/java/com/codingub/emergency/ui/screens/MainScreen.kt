package com.codingub.emergency.ui.screens


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.codingub.emergency.ui.theme.EmergencyTheme

@Composable
fun MainScreen() {

}


@Composable
@Preview(device = "id:pixel_4a", showBackground = true, backgroundColor = 0xFF3A2F6E)
private fun MainScreenPreview() {
    EmergencyTheme {
        MainScreen()
    }
}