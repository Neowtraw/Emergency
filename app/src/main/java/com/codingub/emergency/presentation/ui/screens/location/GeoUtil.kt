package com.codingub.emergency.presentation.ui.screens.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.util.Locale

object GeoUtil {

    fun getAddress(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        return if (!addresses.isNullOrEmpty()) {
            addresses[0].getAddressLine(0)
        } else {
            "Location not found"
        }
    }
}