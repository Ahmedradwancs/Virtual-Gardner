package com.example.virtualgardner.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class SensorDataViewModel : ViewModel() {

    // Separate StateFlows for humidity, soil moisture, and temperature
    private val _humidity = MutableStateFlow<String>("Loading...") // Default value of 0 for humidity
    val humidity: StateFlow<String> = _humidity

    private val _soil = MutableStateFlow<String>("Loading...") // Default value of 0 for soil moisture
    val soil: StateFlow<String> = _soil

    private val _temperature = MutableStateFlow<String>("Loading...") // Default value of 0 for temperature
    val temperature: StateFlow<String> = _temperature

    //for the location as well
    private val _location = MutableStateFlow("Fetching location...")
    val location: StateFlow<String> = _location


    // OkHttpClient instance to make network requests
    private val client = OkHttpClient()

    // Initialize the ViewModel by fetching sensor data
    init {
        fetchSensorData() // Fetch sensor data on initialization
    }

    // Function to fetch sensor data from the Firebase URL
    private fun fetchSensorData() {
        viewModelScope.launch {
            while (true) {
                try {
                    val url =
                        "https://virtual-gardner-default-rtdb.europe-west1.firebasedatabase.app/sensor-data.json"
                    val request = Request.Builder().url(url).build()
                    val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }

                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()

                        if (responseBody != null) {
                            val jsonResponse = JSONObject(responseBody)
                            val humidityValue = jsonResponse.getInt("humidity")
                            val soilValue = jsonResponse.getInt("soil")
                            val temperatureValue = jsonResponse.getInt("temperature")

                            // Emit each sensor value to the corresponding StateFlow
                            _humidity.emit(humidityValue.toString())
                            _soil.emit(soilValue.toString())
                            _temperature.emit(temperatureValue.toString())

                            // Location data (dummy data as an example)
                            _location.emit("Storgatan 96, Kristianstad, Skåne Lan 29526")
                        }
                    } else {
                        // Handle error response
                        _humidity.emit("No data available.")
                        _soil.emit("No data available.")
                        _temperature.emit("No data available.")

                        _location.emit("Location unavailable")


                    }
                } catch (e: Exception) {
                    // Handle exceptions during the network request
                    _humidity.emit("Failed to load data.")
                    _soil.emit("Failed to load data.")
                    _temperature.emit("Failed to load data.")

                    _location.emit("Failed to load location data")
                }


                delay(5000) // Fetch sensor data every 5 seconds

            }
        }
    }
}
