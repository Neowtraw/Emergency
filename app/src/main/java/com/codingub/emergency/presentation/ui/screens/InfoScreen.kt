package com.codingub.emergency.presentation.ui.screens

import android.Manifest
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.codingub.emergency.R
import com.codingub.emergency.common.ResultState
import com.codingub.emergency.domain.models.Service
import com.codingub.emergency.presentation.ui.customs.CountryDropDownMenu
import com.codingub.emergency.presentation.ui.customs.getBackgroundBrush
import com.codingub.emergency.presentation.ui.screens.location.GeoUtil
import com.codingub.emergency.presentation.ui.screens.location.PermissionRationaleDialog
import com.codingub.emergency.presentation.ui.screens.location.PermissionRequestButton
import com.codingub.emergency.presentation.ui.screens.location.RationaleState
import com.codingub.emergency.presentation.ui.utils.Constants.INFO_HEADER_TEXT
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_CONTENT_TEXT
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_DIVIDER
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_DIVIDER_ITEMS
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_HEADER_TEXT
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_PADDING
import com.codingub.emergency.presentation.ui.utils.ScreenState
import com.codingub.emergency.presentation.ui.viewmodels.InfoViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.collectLatest
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
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

    val fineLocationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
        ),
    )

    var rationaleState by remember {
        mutableStateOf<RationaleState?>(null)
    }

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
        rationaleState?.run { PermissionRationaleDialog(rationaleState = this) }

        CountryDropDownMenu(
            code = country,
            modifier = Modifier.fillMaxWidth(),
            isLabelVisible = false,
            value = country.name
        ) {
            Log.d("country", it.language)
            infoViewModel.setCountry(it)
        }
        Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))

        Text(
            text = stringResource(id = R.string.emergency_service),
            fontSize = MAIN_HEADER_TEXT.sp,
            fontWeight = FontWeight.ExtraBold,
            color = colorResource(id = R.color.contrast)
        )
        Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))

        when (screenState) {
            is ScreenState.Loading -> {}
            is ScreenState.Success -> {
                PhoneGrid(infoViewModel.services.collectAsState().value.data!!) {}
            }

            is ScreenState.Error -> {}
        }
        Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))
        Text(
            text = stringResource(id = R.string.user_statistic),
            fontSize = MAIN_HEADER_TEXT.sp,
            fontWeight = FontWeight.ExtraBold,
            color = colorResource(id = R.color.contrast)
            )
        Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))

        PermissionRequestButton(
            isGranted = fineLocationPermissionState.allPermissionsGranted,
            title = "Precise location access",
            onClick = {
                if (fineLocationPermissionState.shouldShowRationale) {
                    rationaleState = RationaleState(
                        "Request Precise Location",
                        "In order to use this feature please grant access by accepting " + "the location permission dialog." + "\n\nWould you like to continue?",
                    ) { proceed ->
                        if (proceed) {
                            fineLocationPermissionState.launchMultiplePermissionRequest()
                        }
                        rationaleState = null
                    }
                } else {
                    fineLocationPermissionState.launchMultiplePermissionRequest()
                }
            },
            onGranted = {
                LocationUpdatesContent(true)
            }
        )
        Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))
        InfoHeaderText(text = "Ваш домашний адрес")
        InfoContentText(text = infoViewModel.getUser().address)

        Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))
        if (infoViewModel.getUser().parentNumber != null) {
            InfoHeaderText(text = "Номер родителя")
            InfoContentText(text = infoViewModel.getUser().parentNumber!!)
        }
    }
}

@Composable
fun InfoHeaderText(text: String) {
    Text(text = text,
        color = colorResource(id = R.color.main_text),
        fontSize = INFO_HEADER_TEXT.sp,
        fontWeight = FontWeight.Medium)
}

@Composable
fun InfoContentText(text: String) {
    Text(text = text,
        color = colorResource(id = R.color.main_text),
        fontSize = MAIN_CONTENT_TEXT.sp,
        fontWeight = FontWeight.Normal)
}

@Composable
@SuppressWarnings("MissingPermission")
fun LocationUpdatesContent(usePreciseLocation: Boolean) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var locationRequest by remember {
        mutableStateOf<LocationRequest?>(null)
    }
    var locationUpdates by remember {
        mutableStateOf("")
    }

    DisposableEffect(locationRequest, lifecycleOwner) {
        locationRequest = LocationRequest.Builder(
            if (usePreciseLocation) {
                Priority.PRIORITY_HIGH_ACCURACY
            } else {
                Priority.PRIORITY_BALANCED_POWER_ACCURACY
            },
            TimeUnit.SECONDS.toMillis(3)
        ).build()

        onDispose {
            locationRequest = null
        }
    }
    // Only register the location updates effect when we have a request
    if (locationRequest != null) {
        LocationUpdatesEffect(locationRequest!!) { result ->
            // For each result update the text
            for (currentLocation in result.locations) {
                locationUpdates = GeoUtil.getAddress(
                    context,
                    latitude = currentLocation.latitude,
                    longitude = currentLocation.longitude
                )
            }
        }
    }

    Column {
        InfoHeaderText(text = "Сейчас вы находитесь")
        InfoContentText(text = locationUpdates)
    }

}


@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)
@Composable
fun LocationUpdatesEffect(
    locationRequest: LocationRequest,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onUpdate: (result: LocationResult) -> Unit,
) {
    val context = LocalContext.current
    val currentOnUpdate by rememberUpdatedState(newValue = onUpdate)

    // Whenever on of these parameters changes, dispose and restart the effect.
    DisposableEffect(locationRequest, lifecycleOwner) {
        val locationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                currentOnUpdate(result)
            }
        }
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                locationClient.requestLocationUpdates(
                    locationRequest, locationCallback, Looper.getMainLooper(),
                )
            } else if (event == Lifecycle.Event.ON_STOP) {
                locationClient.removeLocationUpdates(locationCallback)
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            locationClient.removeLocationUpdates(locationCallback)
            lifecycleOwner.lifecycle.removeObserver(observer)
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
            modifier = Modifier
                .weight(1f)
                .padding(end = MAIN_PADDING.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
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


