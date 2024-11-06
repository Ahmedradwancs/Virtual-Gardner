package com.example.virtualgardner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.virtualgardner.ui.MyAppNavHost
import com.example.virtualgardner.ui.screens.SplashScreen
import com.example.virtualgardner.ui.theme.VirtualGardnerTheme
import com.example.virtualgardner.utils.LocationHelper
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var locationHelper: LocationHelper
    private var userLocation by mutableStateOf("Fetching location...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        locationHelper = LocationHelper(this)

        // Request location permission and fetch location
        checkLocationPermissionAndFetchLocation()

        setContent {
            VirtualGardnerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isSplashVisible by remember { mutableStateOf(true) }

                    if (isSplashVisible) {
                        SplashScreen {
                            isSplashVisible = false
                        }
                    } else {
                        MyAppNavHost(
                            startDestination = if (auth.currentUser != null) "home" else "welcome",
                            auth = auth,
                            location = userLocation
                        )
                    }
                }
            }
        }
    }

    private fun checkLocationPermissionAndFetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Permission already granted, fetch location
            fetchLocation()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            fetchLocation()
        } else {
            userLocation = "Location permission not granted"
        }
    }

    private fun fetchLocation() {
        locationHelper.fetchLocation { location ->
            userLocation = location
        }
    }
}
