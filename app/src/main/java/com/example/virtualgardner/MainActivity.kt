package com.example.virtualgardner

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import android.Manifest


class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var bluetoothConnection: BluetoothConnection
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        // Initialize Bluetooth connection
        bluetoothConnection = BluetoothConnection(this)

        // Initialize the permission launcher
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.all { it.value }) {
                // All permissions granted
                connectToBluetoothDevice()
            } else {
                Log.e("MainActivity", "Bluetooth permissions denied")
            }
        }

        // Request Bluetooth permissions
        requestBluetoothPermissions()

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

    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestBluetoothPermissions() {
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        permissionLauncher.launch(permissions)
    }
    // Function to handle Bluetooth device connection
    @RequiresApi(Build.VERSION_CODES.S)
    private fun connectToBluetoothDevice() {
        val macAddress = "7c:87:ce:ee:2d:fc"
        val isConnected = bluetoothConnection.connectToDevice(macAddress)

        if (isConnected) {
            val sensorData = bluetoothConnection.readFromDevice()
            Log.d("MainActivity", "Sensor Data: $sensorData")
        } else {
            Log.d("MainActivity", "Failed to connect to Bluetooth device")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothConnection.closeConnection()
    }
}