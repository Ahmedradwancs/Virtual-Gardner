package com.example.virtualgardner.data

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.virtualgardner.MainActivity
import java.io.IOException
import java.io.InputStream
import java.util.*

class BluetoothConnection(private val context: MainActivity) {

    private var bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var socket: BluetoothSocket? = null

    @SuppressLint("MissingPermission")
    fun discoverPairedDevices() {
        val pairedDevices: Set<BluetoothDevice>? = this.bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceAddress = device.address // MAC address
            Log.d("Bluetooth", "Device: $deviceName, $deviceAddress")
        }
    }

    fun checkBluetoothPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun <boolean> connectToDevice(deviceAddress: String) {
        if (!checkBluetoothPermissions()) {
            Log.e("Bluetooth", "Bluetooth permissions not granted")
            return
        }

        val device: BluetoothDevice = try {
            bluetoothAdapter?.getRemoteDevice(deviceAddress)
                ?: throw IllegalArgumentException("Bluetooth device not found")
        } catch (e: SecurityException) {
            Log.e("Bluetooth", "Permission denied to get the remote device", e)
            return
        }

        try {
            val uuid: UUID = device.uuids[0].uuid // Check permission before accessing UUID
            socket = device.createRfcommSocketToServiceRecord(uuid)
        } catch (e: SecurityException) {
            Log.e("Bluetooth", "Permission denied to create RFComm socket", e)
            return
        } catch (e: IOException) {
            Log.e("Bluetooth", "Failed to create socket", e)
            return
        }

        try {
            socket?.connect()
        } catch (e: IOException) {
            Log.e("Bluetooth", "Unable to connect to device", e)
            return
        }
    }

    fun readFromDevice(): String? {
        if (!checkBluetoothPermissions()) {
            Log.e("Bluetooth", "Bluetooth permissions not granted")
            return null
        }

        val inputStream: InputStream? = socket?.inputStream
        val buffer = ByteArray(1024)
        var bytes: Int

        return try {
            bytes = inputStream?.read(buffer) ?: 0
            String(buffer, 0, bytes)
        } catch (e: IOException) {
            Log.e("Bluetooth", "Failed to read data from input stream", e)
            null
        } catch (e: SecurityException) {
            Log.e("Bluetooth", "Permission denied to read input stream", e)
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
}
