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
import androidx.compose.ui.unit.Dp
import com.example.virtualgardner.R
import com.example.virtualgardner.ui.components.LogoutButton
import com.example.virtualgardner.ui.theme.gradient
import com.example.virtualgardner.ui.viewmodels.SensorDataViewModel

@Composable
fun PlantMonitoringUI(
    viewModel: SensorDataViewModel = remember { SensorDataViewModel() },
    onLogoutClick: () -> Unit

) {
    val humidityData by viewModel.humidity.collectAsState()
    val soilData by viewModel.soil.collectAsState()
    val temperatureData by viewModel.temperature.collectAsState()
    val locationData by viewModel.location.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),  // background

    ) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp) // Add vertical padding to position content lower
          
    ) {
        // Header Section
        Text(
            text = "Plant Monitoring Dashboard",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF5F5DC),
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )
        Text(
            text = "The real-time health and status of your plants with live sensor data.",
            fontSize = 18.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 20.dp)        )

        // Cards Section with increased height for each card
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                MonitoringCard(
                    titleIcon = R.drawable.humidity,
                    title = "Humidity Report",
                    data = "Humidity: ${humidityData ?: "N/A"}%",
                    dataIcon = R.drawable.humidity,
                    cardHeight = 150.dp
                )
            }

            item {
                MonitoringCard(
                    titleIcon = R.drawable.moisture,
                    title = "Soil Moisture Data",
                    data = "Soil Moisture: ${soilData ?: "N/A"}%",
                    dataIcon = R.drawable.moisture,
                    cardHeight = 150.dp
                )
            }

            item {
                MonitoringCard(
                    titleIcon = R.drawable.temperature,
                    title = "Temperature Data",
                    data = "Temperature: ${temperatureData ?: "N/A"}°C",
                    dataIcon = R.drawable.temp,
                    cardHeight = 150.dp
                )
            }

            item {
                MonitoringCard(
                    titleIcon = R.drawable.location,
                    title = "Location",
                    data = locationData ?: "Location not available",
                    dataIcon = R.drawable.location,
                    cardHeight = 180.dp
                )
            }
        }
    }
        // Wrapper Box for positioning LogoutButton
        Box(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
        ) {
            LogoutButton(onLogoutClick = onLogoutClick)
        }
    }
}

@Composable
fun MonitoringCard(
    titleIcon: Int,
    title: String,
    data: String,
    dataIcon: Int,
    cardHeight: Dp
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .padding(horizontal = 6.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5EACD)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Section: Title and main icon
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = titleIcon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }

            // Divider between title and data section
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.Gray,
                thickness = 1.dp
            )

            // Bottom Section: Data with data icon
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = dataIcon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = data,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview
@Composable
fun PlantMonitoringUIPreview() {
    PlantMonitoringUI(onLogoutClick = {})
}
