package com.codingub.emergency.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.codingub.emergency.R
import com.codingub.emergency.common.ResultState
import com.codingub.emergency.domain.models.Service
import com.codingub.emergency.presentation.ui.customs.CountryDropDownMenu
import com.codingub.emergency.presentation.ui.customs.getBackgroundBrush
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_CONTENT_TEXT
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_DIVIDER
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_DIVIDER_ITEMS
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_HEADER_TEXT
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_PADDING
import com.codingub.emergency.presentation.ui.utils.ScreenState
import com.codingub.emergency.presentation.ui.viewmodels.InfoViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun InfoScreen(
    navController: NavController,
    infoViewModel: InfoViewModel = hiltViewModel()
) {

    val country by infoViewModel.country.collectAsState()
    var screenState by remember {
        mutableStateOf<ScreenState<List<Service>>>(ScreenState.Loading)
    }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = context) {

        infoViewModel.services.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).collectLatest { result ->
            Log.d("country", result.toString())

            when (result) {
                is ResultState.Success -> screenState = ScreenState.Success(data = result.data!!)
                is ResultState.Error -> screenState = ScreenState.Error(error = result.error!!)
                is ResultState.Loading -> screenState = ScreenState.Loading
            }
        }
    }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .background(getBackgroundBrush())
            .padding(top = 40.dp)
            .padding(horizontal = MAIN_PADDING.dp)

    ) {
        CountryDropDownMenu(
            code = country,
            modifier = Modifier.fillMaxWidth(),
            isLabelVisible = false,
            value = country.name
        ) {
            infoViewModel.setCountry(it)
        }
        Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))

        Text(
            text = stringResource(id = R.string.emergency_service),
            fontSize = MAIN_HEADER_TEXT.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))

        when (screenState) {
            is ScreenState.Loading -> {}
            is ScreenState.Success -> {
                PhoneGrid(infoViewModel.services.collectAsState().value.data!!) {}
            }
            is ScreenState.Error -> {}
        }
    }
}


@Composable
private fun PhoneGrid(phones: List<Service>, onPhoneClicked: () -> Unit) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(MAIN_DIVIDER_ITEMS.dp)
    ) {
        items(phones) { phone ->
            PhoneItem(
                title = phone.name,
                phone = phone.phone,
                onIconClicked = { onPhoneClicked() }
            )
        }
    }
}

@Composable
private fun PhoneItem(
    title: String,
    phone: String,
    onIconClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = colorResource(id = R.color.main_text),
                fontWeight = FontWeight.SemiBold,
                fontSize = MAIN_CONTENT_TEXT.sp
            )
            Text(
                text = phone,
                color = colorResource(id = R.color.main_text),
                fontSize = MAIN_CONTENT_TEXT.sp
            )
        }

        Box(
            modifier = Modifier
                .size(40.dp)
                //  .shadow(MAIN_ELEVATION.dp)
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


