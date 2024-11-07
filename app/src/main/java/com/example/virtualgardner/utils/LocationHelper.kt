package com.example.virtualgardner.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class LocationHelper(private val activity: Activity) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)
    private var locationCallback: ((String) -> Unit)? = null

    fun fetchLocation(callback: (String) -> Unit) {
        locationCallback = callback
        // Check if permission is granted before attempting to access location
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, invoke the callback with a suitable message
            locationCallback?.invoke("Location permission not granted")
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val geocoder = Geocoder(activity, Locale.getDefault())
                val addressList = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                val address = addressList?.get(0)?.getAddressLine(0) ?: "Location not available"
                locationCallback?.invoke(address)
            } ?: run {
                locationCallback?.invoke("Location not available")
            }
        }
    }
}
