package com.example.virtualgardner.data


import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.virtualgardner.MainActivity
import java.io.IOException
import java.io.InputStream
import java.util.*

class BluetoothConnection(private val context: MainActivity) {

    private var bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var socket: BluetoothSocket? = null

    companion object {
        private const val REQUEST_BLUETOOTH_PERMISSIONS = 1
    }

    // Function to check and request Bluetooth permissions
    @RequiresApi(Build.VERSION_CODES.S)
    fun checkAndRequestBluetoothPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

        return if (missingPermissions.isNotEmpty()) {
            // Request permissions
            ActivityCompat.requestPermissions(context, missingPermissions.toTypedArray(), REQUEST_BLUETOOTH_PERMISSIONS)
            false // Permissions not granted yet, requested
        } else {
            true // Permissions already granted
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    fun connectToDevice(deviceAddress: String): Boolean {
        if (!checkAndRequestBluetoothPermissions()) {
            Log.e("Bluetooth", "Bluetooth permissions not granted")
            return false
        }

        val device: BluetoothDevice = try {
            bluetoothAdapter?.getRemoteDevice(deviceAddress)
                ?: throw IllegalArgumentException("Bluetooth device not found")
        } catch (e: SecurityException) {
            Log.e("Bluetooth", "Permission denied to get the remote device", e)
            return false
        }

        try {
            val uuid: UUID = device.uuids[0].uuid
            socket = device.createRfcommSocketToServiceRecord(uuid)
        } catch (e: IOException) {
            Log.e("Bluetooth", "Failed to create socket", e)
            return false
        }

        return try {
            socket?.connect()
            true // Connection successful
        } catch (e: IOException) {
            Log.e("Bluetooth", "Unable to connect to device", e)
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun readFromDevice(): String? {
        if (!checkAndRequestBluetoothPermissions()) {
            Log.e("Bluetooth", "Bluetooth permissions not granted")
            return null
        }

        val inputStream: InputStream? = socket?.inputStream
        val buffer = ByteArray(1024)
        return try {
            val bytes: Int = inputStream?.read(buffer) ?: 0
            String(buffer, 0, bytes)
        } catch (e: IOException) {
            Log.e("Bluetooth", "Failed to read data from input stream", e)
            null
        }
    }

    fun closeConnection() {
        try {
            socket?.close()
        } catch (e: IOException) {
            Log.e("Bluetooth", "Failed to close socket", e)
        }
    }
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Log.d("BluetoothConnection", "Bluetooth permissions granted")
            } else {
                Log.e("BluetoothConnection", "Bluetooth permissions denied")
            }
        }
    }
}