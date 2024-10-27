package com.example.virtualgardner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import com.example.virtualgardner.R
import com.example.virtualgardner.ui.viewmodels.SensorDataViewModel

@Composable
fun PlantMonitoringUI(viewModel: SensorDataViewModel = remember { SensorDataViewModel() }) {
    // Observing individual sensor data from ViewModel
    val humidityData by viewModel.humidity.collectAsState()
    val soilData by viewModel.soil.collectAsState()
    val temperatureData by viewModel.temperature.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCBD8B5)) // Background color similar to the image
            .padding(16.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        // Humidity Report Card with real-time data
        item {
            MonitoringCard(
                icon = R.drawable.humidity, // Replace with appropriate drawable
                title = "Humidity Report",
                description = "Humidity: $humidityData%"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Moisture Data Card with real-time data
        item {
            MonitoringCard(
                icon = R.drawable.moisture, // Replace with appropriate drawable
                title = "Soil Moisture Data",
                description = "Soil Moisture: $soilData%"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Temperature Data Card with real-time data
        item {
            MonitoringCard(
                icon = R.drawable.temperature, // Replace with appropriate drawable
                title = "Temperature Data",
                description = "Temperature: $temperatureData°C"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MonitoringCard(icon: Int, title: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5EACD)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.Fit
            )

            // Text
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview
@Composable
fun PlantMonitoringUIPreview() {
    PlantMonitoringUI()
}
