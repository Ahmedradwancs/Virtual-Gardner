package com.example.virtualgardner.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GrowthInsightScreen(humidity: String, temperature: String) {
    // List to hold data points for the graph
    var humidityDataPoints by remember { mutableStateOf(listOf<Float>()) }
    var temperatureDataPoints by remember { mutableStateOf(listOf<Float>()) }
    var timestamps by remember { mutableStateOf(listOf<String>()) }

    // Convert received data to Float and update data points
    val humidityValue = humidity.toFloatOrNull() ?: 0f
    val temperatureValue = temperature.toFloatOrNull() ?: 0f

    LaunchedEffect(humidityValue, temperatureValue) {
        // Limit the number of points for smooth visualization, remove the oldest when reaching the limit
        if (humidityDataPoints.size >= 20) {
            humidityDataPoints = humidityDataPoints.drop(1)
            temperatureDataPoints = temperatureDataPoints.drop(1)
            timestamps = timestamps.drop(1)
        }

        humidityDataPoints = humidityDataPoints + humidityValue
        temperatureDataPoints = temperatureDataPoints + temperatureValue
    }

    // Timer to update X-axis timestamps every 30 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(30000L) // 30 seconds delay
            val currentTime = System.currentTimeMillis()
            val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val formattedTime = formatter.format(Date(currentTime))

            // Limit the number of timestamps to match the max data points
            if (timestamps.size >= 20) {
                timestamps = timestamps.drop(1)
            }
            timestamps = timestamps + formattedTime
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Growth Insight",
            fontSize = 24.sp,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description
        Text(
            text = "Humidity and temperature significantly influence plant growth; " +
                    "ideal humidity supports nutrient uptake, while stable temperatures " +
                    "promote healthy metabolism and photosynthesis.",
            fontSize = 14.sp,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray,
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Real-time Graph Section
        Box(
            modifier = Modifier
                .size(300.dp)
                .padding(16.dp)
        ) {
            // Draw real-time graph with temperature and humidity data points
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val maxDataPoints = 20
                val maxHumidity = 100f // Assuming humidity max value
                val maxTemperature = 50f // Assuming temperature max value

                // Draw Y-Axis labels and grid lines
                for (i in 0..5) {
                    val y = height - (i * height / 5)
                    val label = (i * 20).toString()
                    drawContext.canvas.nativeCanvas.drawText(label, 0f, y, android.graphics.Paint().apply {
                        color = android.graphics.Color.GRAY
                        textSize = 24f
                    })

                    // Draw horizontal grid line
                    drawLine(
                        color = Color.LightGray,
                        start = androidx.compose.ui.geometry.Offset(0f, y),
                        end = androidx.compose.ui.geometry.Offset(width, y),
                        strokeWidth = 1f
                    )
                }

                // Draw X-Axis labels for time
                timestamps.forEachIndexed { index, time ->
                    val x = index * (width / maxDataPoints)
                    drawContext.canvas.nativeCanvas.drawText(
                        time,
                        x,
                        height,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.GRAY
                            textSize = 24f
                        }
                    )
                }

                // Scale data points to fit within the Canvas
                val humidityPath = Path().apply {
                    moveTo(0f, height - (humidityDataPoints.firstOrNull()?.let { it / maxHumidity * height } ?: 0f))
                    humidityDataPoints.forEachIndexed { index, value ->
                        val x = index * (width / maxDataPoints)
                        val y = height - (value / maxHumidity * height)
                        lineTo(x, y)
                    }
                }

                val temperaturePath = Path().apply {
                    moveTo(0f, height - (temperatureDataPoints.firstOrNull()?.let { it / maxTemperature * height } ?: 0f))
                    temperatureDataPoints.forEachIndexed { index, value ->
                        val x = index * (width / maxDataPoints)
                        val y = height - (value / maxTemperature * height)
                        lineTo(x, y)
                    }
                }

                // Draw humidity line (light blue)
                drawPath(
                    path = humidityPath,
                    color = Color.Cyan,
                    style = Stroke(width = 4f)
                )

                // Draw temperature line (red)
                drawPath(
                    path = temperaturePath,
                    color = Color.Red,
                    style = Stroke(width = 4f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Display humidity and temperature readings
        Text(
            text = "Humidity: $humidity %",
            fontSize = 20.sp,
        )
        Text(
            text = "Temperature: $temperature°C",
            fontSize = 20.sp,
        )
    }
}
