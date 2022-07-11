package com.alvayonara.weather.utils

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*

object Map {

    fun getAddressFromLatLong(context: Context, latLng: LatLng): String? {
        val geocode = Geocoder(context, Locale.getDefault())
        return try {
            val addressList =
                geocode.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addressList != null && addressList.size > 0) {
                addressList[0].getAddressLine(0)
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}