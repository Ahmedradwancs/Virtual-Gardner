package com.example.virtualgardner

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.virtualgardner.data.BluetoothConnection
import com.example.virtualgardner.ui.MyAppNavHost
import com.example.virtualgardner.ui.screens.SplashScreen
import com.example.virtualgardner.ui.theme.VirtualGardnerTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var bluetoothConnection: BluetoothConnection
    private val context: MainActivity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val currentUser = auth.currentUser

        bluetoothConnection = BluetoothConnection(context)
        // Discover paired devices
        bluetoothConnection.discoverPairedDevices()
        val macAddress = "7c:87:ce:ee:2d:fc"
        var isConnected = bluetoothConnection.connectToDevice(macAddress)

        if (isConnected) {
            // Read data from the sensor
            val sensorData = bluetoothConnection.readFromDevice()
            Log.d("MainActivity", "Sensor Data: $sensorData")
        } else {
            Log.d("MainActivity", "Failed to connect to Bluetooth device")
        }

        setContent {
            VirtualGardnerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // State to manage splash screen visibility
                    var isSplashVisible by remember { mutableStateOf(true) }

                    // Show splash screen
                    if (isSplashVisible) {
                        SplashScreen {
                            isSplashVisible = false // Hide splash screen when it's done
                        }
                    } else {
                        // After the splash screen, navigate to MyAppNavHost
                        MyAppNavHost(
                            startDestination = if (auth.currentUser != null) "home" else "welcome",
                            auth = auth
                        )
                    }
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        // Close Bluetooth connection when activity is destroyed
        bluetoothConnection.closeConnection()
    }
}