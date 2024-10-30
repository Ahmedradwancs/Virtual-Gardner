/* package com.example.virtualgardner.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.virtualgardner.ui.components.LogoutButton
import com.example.virtualgardner.ui.theme.gradient
import java.util.*
import androidx.compose.ui.unit.sp


@Composable
fun SmellDataScreen(onLogoutClick: () -> Unit) {
    val context = LocalContext.current
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    // State variables for temperature and humidity readings
    var temperatureData by remember { mutableStateOf("No data") }
    var humidityData by remember { mutableStateOf("No data") }
    val temperatureList = remember { mutableStateListOf<Float>() }
    val humidityList = remember { mutableStateListOf<Float>() }

    // State for connection and permissions
    var permissionsGranted by remember { mutableStateOf(false) }
    var permissionsDeniedText by remember { mutableStateOf("") }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            permissionsGranted = granted
            if (!permissionsGranted) {
                permissionsDeniedText = "Location permission is required to access the smell sensor."
            } else {
                startBLEScan(bluetoothLeScanner, context) { device ->
                    connectToDevice(context, device) { data ->
                        when {
                            data.startsWith("Temperature") -> {
                                val temp = data.removePrefix("Temperature: ").toFloatOrNull() ?: return@connectToDevice
                                temperatureData = "$temp °C"
                                temperatureList.add(temp)
                                checkPlantHealth(context, temp, humidityList.lastOrNull() ?: 0f)
                            }
                            data.startsWith("Humidity") -> {
                                val hum = data.removePrefix("Humidity: ").toFloatOrNull() ?: return@connectToDevice
                                humidityData = "$hum %"
                                humidityList.add(hum)
                                checkPlantHealth(context, temperatureList.lastOrNull() ?: 0f, hum)
                            }
                        }
                    }
                }
            }
        }
    )
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {  // Android 10 or lower
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                permissionsGranted = true
                startBLEScan(bluetoothLeScanner, context) { device ->
                    connectToDevice(context, device) { data ->
                        when {
                            data.startsWith("Temperature") -> temperatureData = data.removePrefix("Temperature: ")
                            data.startsWith("Humidity") -> humidityData = data.removePrefix("Humidity: ")
                        }
                    }
                }
            }
        } else {
            permissionsGranted = true
            startBLEScan(bluetoothLeScanner, context) { device ->
                connectToDevice(context, device) { data ->
                    when {
                        data.startsWith("Temperature") -> temperatureData = data.removePrefix("Temperature: ")
                        data.startsWith("Humidity") -> humidityData = data.removePrefix("Humidity: ")
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Wrapper Box for positioning LogoutButton
        Box(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
        ) {
            LogoutButton(onLogoutClick = onLogoutClick)
        }

        // Display Temperature and Humidity readings
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Temperature: $temperatureData °C", fontSize = 24.sp, modifier = Modifier.padding(8.dp))
            Text(text = "Humidity: $humidityData %", fontSize = 24.sp, modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.height(24.dp))
            TemperatureHumidityGraph(temperatureList, humidityList)
        }
    }
}

@Composable
fun TemperatureHumidityGraph(temperatureData: List<Float>, humidityData: List<Float>) {
    val dataPoints = temperatureData.zip(humidityData)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(16.dp)
    ) {
        val padding = 32.dp.toPx()
        val xInterval = (size.width - 2 * padding) / (dataPoints.size - 1).coerceAtLeast(1)
        val maxY = humidityData.maxOrNull() ?: 1f
        val yScale = (size.height - 2 * padding) / maxY

          // Draw X and Y axes
        drawLine(
            color = Color.Black,
            start = androidx.compose.ui.geometry.Offset(padding, size.height - padding),
            end = androidx.compose.ui.geometry.Offset(size.width - padding, size.height - padding),
            strokeWidth = 4.dp.toPx()
        )
        drawLine(
            color = Color.Black,
            start = androidx.compose.ui.geometry.Offset(padding, padding),
            end = androidx.compose.ui.geometry.Offset(padding, size.height - padding),
            strokeWidth = 4.dp.toPx()
        )
        val path = Path().apply {
            moveTo(padding, size.height - padding - (dataPoints.firstOrNull()?.second ?: 0f) * yScale)
            dataPoints.forEachIndexed { index, (_, humidity) ->
                val x = padding + index * xInterval
                val y = size.height - padding - (humidity * yScale)
                lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = Color.Blue,
            style = Stroke(width = 4.dp.toPx())
        )
    }
}
private fun checkPlantHealth(context: Context, temperature: Float, humidity: Float) {
    val temperatureThreshold = 25.0f
    val humidityThreshold = 50.0f

    if (temperature > temperatureThreshold && humidity > humidityThreshold) {
        Toast.makeText(context, "Plant growth is good. No need to water.", Toast.LENGTH_LONG).show()
    } else {
        Toast.makeText(context, "Water the plant soon. Conditions are not optimal.", Toast.LENGTH_LONG).show()
    }
}


// Function to start BLE scan and connect to "Smell Inspector"
private fun startBLEScan(
    bluetoothLeScanner: BluetoothLeScanner?,
    context: Context,
    onDeviceFound: (BluetoothDevice) -> Unit
) {
    if (bluetoothLeScanner == null) {
        Log.d("SmellDataScreen", "Bluetooth scanner not available.")
        return
    }

    // Callback for BLE scan results
    val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val deviceName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                "Unknown"
            } else {
                device.name ?: "Unknown"
            }

            if (deviceName == "Smell Inspector") {
                Log.d("SmellDataScreen", "Found device: $deviceName, Address: ${device.address}")
                bluetoothLeScanner.stopScan(this)
                onDeviceFound(device)
            }
        }
    }

    // Start the scan
    bluetoothLeScanner.startScan(scanCallback)
    Log.d("SmellDataScreen", "BLE scan started.")
}


private const val MAX_RETRIES = 3  // Maximum number of retry attempts
private val SERVICE_UUID = UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b")
private val RESISTANCE_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8")
private val TEMPERATURE_UUID = UUID.fromString("688091db-1736-4179-b7ce-e42a724a6a68")
private val HUMIDITY_UUID = UUID.fromString("0515e27d-dd91-4f96-9452-5f43649c1819")

private var connectionAttempts = 0

private fun connectToDevice(
    context: Context,
    device: BluetoothDevice,
    onDataReceived: (String) -> Unit
) {
    // Check for BLUETOOTH_CONNECT permission if running on Android 12+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
        ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
    ) {
        Log.d("SmellDataScreen", "BLUETOOTH_CONNECT permission not granted.")
        return
    }

    val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    Log.d("SmellDataScreen", "Connected to ${device.name}")
                    connectionAttempts = 0  // Reset retry counter on successful connection
                    gatt.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.d("SmellDataScreen", "Disconnected from ${device.name} with status $status")
                    gatt.close()

                    // Retry connection if we haven't reached the maximum attempts
                    if (connectionAttempts < MAX_RETRIES) {
                        connectionAttempts++
                        Log.d("SmellDataScreen", "Retrying connection (${connectionAttempts} of $MAX_RETRIES)...")
                        retryConnection(context, device, onDataReceived)
                    } else {
                        Log.d("SmellDataScreen", "Max retries reached. Connection failed.")
                    }
                }
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("SmellDataScreen", "Services discovered on ${device.name}")
                val service = gatt.getService(SERVICE_UUID)

                if (service != null) {
                    // Sequentially read Resistance and set up notifications for Temperature and Humidity
                    readCharacteristic(gatt, service, RESISTANCE_UUID, onDataReceived, "Resistance")
                    enableCharacteristicNotification(gatt, service, TEMPERATURE_UUID, onDataReceived, "Temperature")
                    enableCharacteristicNotification(gatt, service, HUMIDITY_UUID, onDataReceived, "Humidity")
                } else {
                    Log.d("SmellDataScreen", "Service UUID not found on device")
                }
            } else {
                Log.d("SmellDataScreen", "Service discovery failed with status $status")
            }
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val data = characteristic.value?.let { String(it) } ?: "No data"
                val charUUID = characteristic.uuid.toString()
                val dataType = when (charUUID) {
                    RESISTANCE_UUID.toString() -> "Resistance"
                    TEMPERATURE_UUID.toString() -> "Temperature"
                    HUMIDITY_UUID.toString() -> "Humidity"
                    else -> "Unknown"
                }
                Log.d("SmellDataScreen", "$dataType data received: $data")
                onDataReceived("$dataType: $data")
            } else {
                Log.d("SmellDataScreen", "Failed to read characteristic with UUID ${characteristic.uuid}")
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            val data = characteristic.value?.let { String(it) } ?: "No data"
            val charUUID = characteristic.uuid.toString()
            val dataType = when (charUUID) {
                TEMPERATURE_UUID.toString() -> "Temperature"
                HUMIDITY_UUID.toString() -> "Humidity"
                else -> "Unknown"
            }
            Log.d("SmellDataScreen", "$dataType data received via notification: $data")
            onDataReceived("$dataType: $data")
        }
    }

    // Initiate connection
    connectionAttempts = 0  // Reset connection attempts on new connection
    device.connectGatt(context, false, gattCallback)
}

// Helper function to retry connection with delay
private fun retryConnection(context: Context, device: BluetoothDevice, onDataReceived: (String) -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        connectToDevice(context, device, onDataReceived)
    }, 3000)  // Retry after 3 seconds
}

// Helper function to read a characteristic
@SuppressLint("MissingPermission")
private fun readCharacteristic(
    gatt: BluetoothGatt,
    service: BluetoothGattService,
    characteristicUUID: UUID,
    onDataReceived: (String) -> Unit,
    dataType: String
) {
    val characteristic = service.getCharacteristic(characteristicUUID)
    if (characteristic != null && (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_READ) != 0) {
        val success = gatt.readCharacteristic(characteristic)
        if (success) {
            Log.d("SmellDataScreen", "Reading $dataType characteristic")
        } else {
            Log.d("SmellDataScreen", "Failed to initiate reading $dataType characteristic")
        }
    } else {
        Log.d("SmellDataScreen", "$dataType characteristic UUID not found or not readable")
    }
}

// Helper function to enable notifications for a characteristic
@SuppressLint("MissingPermission")
private fun enableCharacteristicNotification(
    gatt: BluetoothGatt,
    service: BluetoothGattService,
    characteristicUUID: UUID,
    onDataReceived: (String) -> Unit,
    dataType: String
) {
    val characteristic = service.getCharacteristic(characteristicUUID)
    if (characteristic != null && (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
        gatt.setCharacteristicNotification(characteristic, true)
        Log.d("SmellDataScreen", "Enabling notification for $dataType characteristic")
    } else {
        Log.d("SmellDataScreen", "$dataType characteristic UUID not found or not notifiable")
    }
}

*/
// hardcoded version for data visualization from smell sensor

package com.example.virtualgardner.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.virtualgardner.ui.components.LogoutButton
import com.example.virtualgardner.ui.theme.gradient
import java.util.*

@Composable
fun SmellDataScreen(onLogoutClick: () -> Unit) {
    val context = LocalContext.current
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    // Hardcoded temperature and humidity data for testing
    val temperatureList = listOf(22.5f, 23.0f, 24.0f, 25.5f, 26.0f)
    val humidityList = listOf(55.0f, 58.0f, 60.0f, 62.0f, 65.0f)
    val temperatureData = "${temperatureList.last()} °C"
    val humidityData = "${humidityList.last()} %"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp)
    ) {
        // Wrapper Box for title and positioning LogoutButton
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Growth Insight", fontSize = 24.sp, color = Color.White)
            LogoutButton(onLogoutClick = onLogoutClick)


        }

        // Display Temperature and Humidity readings and Graph
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Temperature: $temperatureData", fontSize = 24.sp, modifier = Modifier.padding(8.dp), color = Color.White)
            Text(text = "Humidity: $humidityData", fontSize = 24.sp, modifier = Modifier.padding(8.dp), color = Color.White)

            Spacer(modifier = Modifier.height(24.dp))

            // Graph
            TemperatureHumidityGraph(temperatureList, humidityList)

            // Notification based on humidity level
            Text(
                text = "Based on the live sensor updates:",
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp)
            )
            val notificationMessage = if (humidityList.last() >= 60) {
                "Growth rate of your plant is good.\nNo need to water."
            } else {
                "Water the plant soon to ensure optimal growth."
            }
            Text(text = notificationMessage, fontSize = 18.sp, color = Color.DarkGray, modifier = Modifier.padding(16.dp))
        }
    }
}

private const val MAX_RETRIES = 3  // Maximum number of retry attempts
private val SERVICE_UUID = UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b")
private val TEMPERATURE_UUID = UUID.fromString("688091db-1736-4179-b7ce-e42a724a6a68")
private val HUMIDITY_UUID = UUID.fromString("0515e27d-dd91-4f96-9452-5f43649c1819")

private var connectionAttempts = 0

// Start Bluetooth Low Energy (BLE) scan and connect to "Smell Inspector" device
private fun startBLEScan(
    bluetoothLeScanner: BluetoothLeScanner?,
    context: Context,
    onDeviceFound: (BluetoothDevice) -> Unit
) {
    if (bluetoothLeScanner == null) {
        Log.d("SmellDataScreen", "Bluetooth scanner not available.")
        return
    }

    // Callback for BLE scan results
    val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val deviceName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                "Unknown"
            } else {
                device.name ?: "Unknown"
            }

            if (deviceName == "Smell Inspector") {
                Log.d("SmellDataScreen", "Found device: $deviceName, Address: ${device.address}")
                bluetoothLeScanner.stopScan(this)
                onDeviceFound(device)
            }
        }
    }

    // Start the scan
    bluetoothLeScanner.startScan(scanCallback)
    Log.d("SmellDataScreen", "BLE scan started.")
}

@SuppressLint("MissingPermission")
private fun connectToDevice(
    context: Context,
    device: BluetoothDevice,
    onDataReceived: (String) -> Unit
) {
    // Check for BLUETOOTH_CONNECT permission if running on Android 12+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
        ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
    ) {
        Log.d("SmellDataScreen", "BLUETOOTH_CONNECT permission not granted.")
        return
    }

    val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    Log.d("SmellDataScreen", "Connected to ${device.name}")
                    connectionAttempts = 0  // Reset retry counter on successful connection
                    gatt.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.d("SmellDataScreen", "Disconnected from ${device.name} with status $status")
                    gatt.close()

                    // Retry connection if we haven't reached the maximum attempts
                    if (connectionAttempts < MAX_RETRIES) {
                        connectionAttempts++
                        Log.d("SmellDataScreen", "Retrying connection (${connectionAttempts} of $MAX_RETRIES)...")
                        retryConnection(context, device, onDataReceived)
                    } else {
                        Log.d("SmellDataScreen", "Max retries reached. Connection failed.")
                    }
                }
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            val service = gatt.getService(SERVICE_UUID)
            service?.let {
                enableCharacteristicNotification(gatt, service, TEMPERATURE_UUID, onDataReceived, "Temperature")
                enableCharacteristicNotification(gatt, service, HUMIDITY_UUID, onDataReceived, "Humidity")
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            val data = characteristic.value?.let { String(it) } ?: "No data"
            val dataType = when (characteristic.uuid) {
                TEMPERATURE_UUID -> "Temperature"
                HUMIDITY_UUID -> "Humidity"
                else -> "Unknown"
            }
            onDataReceived("$dataType: $data")
        }
    }

    device.connectGatt(context, false, gattCallback)
}

// Helper function to retry connection with delay
private fun retryConnection(context: Context, device: BluetoothDevice, onDataReceived: (String) -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        connectToDevice(context, device, onDataReceived)
    }, 3000)  // Retry after 3 seconds
}

// Helper function to enable notifications for a characteristic
@SuppressLint("MissingPermission")
private fun enableCharacteristicNotification(
    gatt: BluetoothGatt,
    service: BluetoothGattService,
    characteristicUUID: UUID,
    onDataReceived: (String) -> Unit,
    dataType: String
) {
    val characteristic = service.getCharacteristic(characteristicUUID)
    characteristic?.let {
        gatt.setCharacteristicNotification(characteristic, true)
        Log.d("SmellDataScreen", "Enabling notification for $dataType characteristic")
    }
}

@Composable
fun TemperatureHumidityGraph(temperatureData: List<Float>, humidityData: List<Float>) {
    val dataPoints = temperatureData.zip(humidityData)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(16.dp)
    ) {
        val padding = 32.dp.toPx()
        val xInterval = (size.width - 2 * padding) / (dataPoints.size - 1).coerceAtLeast(1)
        val maxY = humidityData.maxOrNull() ?: 1f
        val yScale = (size.height - 2 * padding) / maxY

        // Draw X and Y axes
        drawLine(
            color = Color.Black,
            start = androidx.compose.ui.geometry.Offset(padding, size.height - padding),
            end = androidx.compose.ui.geometry.Offset(size.width - padding, size.height - padding),
            strokeWidth = 4.dp.toPx()
        )
        drawLine(
            color = Color.Black,
            start = androidx.compose.ui.geometry.Offset(padding, padding),
            end = androidx.compose.ui.geometry.Offset(padding, size.height - padding),
            strokeWidth = 4.dp.toPx()
        )

        // Draw graph line
        val path = Path().apply {
            moveTo(padding, size.height - padding - (dataPoints.firstOrNull()?.second ?: 0f) * yScale)
            dataPoints.forEachIndexed { index, (_, humidity) ->
                val x = padding + index * xInterval
                val y = size.height - padding - (humidity * yScale)
                lineTo(x, y)
            }
        }
        drawPath(
            path = path,
            color = Color.Blue,
            style = Stroke(width = 2.dp.toPx())
        )

        // Draw x-axis and y-axis labels
        drawIntoCanvas { canvas ->
            val labelPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 42f
            }
            canvas.nativeCanvas.drawText("Temperature (°C)", size.width / 2, size.height - padding / 2, labelPaint)
            canvas.nativeCanvas.rotate(-90f, padding / 2, size.height / 2)
            canvas.nativeCanvas.drawText("Humidity (%)", padding / 2, size.height / 2, labelPaint)
        }
    }

}
