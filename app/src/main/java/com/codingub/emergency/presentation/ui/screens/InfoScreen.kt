package com.codingub.emergency.presentation.ui.screens

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codingub.emergency.R
import com.codingub.emergency.domain.models.Service
import com.codingub.emergency.presentation.ui.customs.ActionBar
import com.codingub.emergency.presentation.ui.customs.CountryDropDownMenu
import com.codingub.emergency.presentation.ui.customs.ErrorStateView
import com.codingub.emergency.presentation.ui.customs.HeaderText
import com.codingub.emergency.presentation.ui.customs.InfoContentText
import com.codingub.emergency.presentation.ui.customs.InfoHeaderText
import com.codingub.emergency.presentation.ui.customs.MemoView
import com.codingub.emergency.presentation.ui.customs.getBackgroundBrush
import com.codingub.emergency.presentation.ui.screens.location.GeoUtil
import com.codingub.emergency.presentation.ui.screens.location.PermissionRationaleDialog
import com.codingub.emergency.presentation.ui.screens.location.PermissionRequestButton
import com.codingub.emergency.presentation.ui.screens.location.RationaleState
import com.codingub.emergency.presentation.ui.theme.monFamily
import com.codingub.emergency.presentation.ui.utils.Constants
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_CONTENT_TEXT
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_DIVIDER
import com.codingub.emergency.presentation.ui.utils.Constants.MAIN_PADDING
import com.codingub.emergency.presentation.ui.utils.shimmerEffect
import com.codingub.emergency.presentation.ui.viewmodels.InfoViewModel
import com.codingub.emergency.presentation.ui.viewmodels.ServiceState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask


@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun InfoScreen() {

    val infoViewModel: InfoViewModel = hiltViewModel()
    val country by infoViewModel.country.collectAsState()
    val fineLocationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
        ),
    )
    val callPermissionState = rememberPermissionState(
        permission =
        Manifest.permission.CALL_PHONE
    )

    SideEffect {
        if (!callPermissionState.status.isGranted) {
            callPermissionState.launchPermissionRequest()
        }
    }

    val serviceState by infoViewModel.serviceState.collectAsStateWithLifecycle()

    Column(Modifier.verticalScroll(rememberScrollState())) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .background(getBackgroundBrush())
                .padding(top = 40.dp, bottom = 70.dp)
        ) {

            ActionBar(text = R.string.emergency_service)

            Column(Modifier.padding(horizontal = MAIN_PADDING.dp)) {
                Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))

                CountryDropDownMenu(
                    code = country,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MAIN_PADDING.dp),
                    isLabelVisible = false,
                    value = country.name
                ) {
                    infoViewModel.setCountry(it)
                }
                Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))

                ServiceContent(states = serviceState)
                Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))
                HeaderText(
                    text = R.string.title_your_data, color = R.color.contrast
                )
                Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))

                // User Location
                InfoHeaderText(text = stringResource(id = R.string.title_current_address))
                LocationPermission(fineLocationPermissionState = fineLocationPermissionState)

                Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))

                // Home Location
                InfoHeaderText(text = stringResource(id = R.string.title_home_address))
                InfoContentText(text = infoViewModel.getUser().address)

                Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))

                // Parent Phone
                if (infoViewModel.getUser().parentNumber != null) {
                    InfoHeaderText(text = stringResource(id = R.string.title_parent_phone))
                    PhoneItem(
                        title = infoViewModel.getUser().phone,
                        phone = infoViewModel.getUser().phone,
                        callPermissionState = callPermissionState
                    )
                }
                Spacer(modifier = Modifier.height(MAIN_DIVIDER.dp))
                MemoView(R.string.title_service_algorithm, R.string.service_algorithm)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun callPermission(
    context: Context,
    callPermissionsState: PermissionState,
    phone: String,
    onProceed: () -> Unit
) {

    if (callPermissionsState.status.isGranted) {
        callPhone(phone, context)
    } else {
        if (callPermissionsState.status.shouldShowRationale) {
            onProceed()
        } else {
            callPermissionsState.launchPermissionRequest()
        }
    }
}

private fun callPhone(phone: String, context: Context) {
    val intent = Intent(Intent.ACTION_CALL)
    intent.data = Uri.parse("tel:$phone")

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}

@Composable
private fun ServiceContent(
    states: ServiceState
) {


    AnimatedContent(targetState = states, label = "") { state ->

        Column {
            when (state) {
                is ServiceState.Loading -> {
                    ServiceLoadingStateView()
                }

                is ServiceState.Result -> {
                    PhoneList(state.services)
                }

                is ServiceState.Error -> {
                    ErrorStateView(message = state.message)
                }

                is ServiceState.NetworkLost -> {
                    ErrorStateView(icon = R.drawable.ic_network_lost,
                       message = R.string.exception_network_lost)
                }
            }
        }
    }
}

@Composable
private fun ServiceLoadingStateView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        (0..4).forEach {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .height(50.dp)
                    .width(50.dp)
                    .shimmerEffect()
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LocationPermission(
    fineLocationPermissionState: MultiplePermissionsState
) {
    var rationaleState by remember {
        mutableStateOf<RationaleState?>(null)
    }

    rationaleState?.run { PermissionRationaleDialog(rationaleState = this) }

    PermissionRequestButton(
        isGranted = fineLocationPermissionState.allPermissionsGranted,
        title = "Precise location access",
        onClick = {
            if (fineLocationPermissionState.shouldShowRationale) {
                rationaleState = RationaleState(
                    "Request Precise Location",
                    "In order to use this feature please grant access by accepting " +
                            "the location permission dialog." + "\n\nWould you like to continue?",
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


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PhoneList(phones: List<Service>) {

    val callPermissionState = rememberPermissionState(
        permission =
        Manifest.permission.CALL_PHONE
    )

    Column {
        (1 until phones.size).forEach {
            PhoneItem(
                title = phones[it].name,
                phone = phones[it].phone,
                callPermissionState = callPermissionState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
private fun PhoneItem(
    callPermissionState: PermissionState,
    title: String,
    phone: String
) {
    val context = LocalContext.current
    var rationaleState by remember {
        mutableStateOf<RationaleState?>(null)
    }

    rationaleState?.run { PermissionRationaleDialog(rationaleState = this) }


    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp),
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
                fontFamily = monFamily,
                fontSize = MAIN_CONTENT_TEXT.sp
            )
            Text(
                text = phone,
                fontFamily = monFamily,
                color = colorResource(id = R.color.main_text),
                fontSize = MAIN_CONTENT_TEXT.sp
            )
        }

        Card(
            modifier = Modifier.size(40.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            shape = RoundedCornerShape(Constants.MAIN_CORNER.dp),
            colors = CardDefaults.cardColors(
                contentColor = colorResource(id = R.color.call),
                containerColor = colorResource(id = R.color.call)
            ),
            onClick = {
                callPermission(context, callPermissionState, phone) {
                    rationaleState = RationaleState(
                        "Запросить разрешение на звонки",
                        "Это позволит вам звонить в экстренные службы прямо с приложения",
                    ) { proceed ->
                        if (proceed) {
                            callPermissionState.launchPermissionRequest()
                        }
                        rationaleState = null
                    }
                }
            }
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
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
}


//fun LazyListScope.phoneList(phones: List<Service>, onPhoneClicked: () -> Unit) {
//    items(phones) { phone ->
//        PhoneItem(
//            title = phone.name,
//            phone = phone.phone,
//            onIconClicked = { onPhoneClicked() }
//        )
//    }
//}


