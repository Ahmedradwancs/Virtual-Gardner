import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.virtualgardner.R
import com.example.virtualgardner.ui.components.LogoutButton
import com.example.virtualgardner.ui.theme.gradient
import com.example.virtualgardner.ui.viewmodels.SensorDataViewModel
import android.app.PendingIntent
import android.content.Intent
import com.example.virtualgardner.MainActivity

// Define the thresholds
const val HUMIDITY_THRESHOLD = 45
const val TEMPERATURE_THRESHOLD = 25

@Composable
fun MonitorAndNotify(
    context: Context,
    humidity: String,
    temperature: String
) {
    LaunchedEffect(humidity, temperature) {
        // Convert humidity and temperature to integer values if not null
        val humidityInt = humidity.toIntOrNull() ?: 0
        val temperatureInt = temperature.toIntOrNull() ?: 0

        // Check if humidity or temperature exceeds the thresholds
        if (humidityInt > HUMIDITY_THRESHOLD || temperatureInt > TEMPERATURE_THRESHOLD) {
            showNotification(
                context = context,
                title = "Plant Alert",
                message = "High levels detected! Humidity: $humidity%, Temperature: $temperature°C"
            )
        }
    }
}

fun showNotification(context: Context, title: String, message: String) {
    val channelId = "plant_monitoring_channel"

    // Create the notification channel on Android O and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Plant Monitoring Alerts",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications for plant monitoring alerts"
        }
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    // Create an intent that opens the app’s main activity
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Build the notification with the pending intent
    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.temperature)  // Replace with your notification icon
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)  // Set the pending intent here
        .setAutoCancel(true)  // Automatically dismiss the notification when clicked
        .build()

    NotificationManagerCompat.from(context).notify(0, notification)
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


@Composable
fun PlantMonitoringUI(
    viewModel: SensorDataViewModel = remember { SensorDataViewModel() },
    onLogoutClick: () -> Unit
) {
    val context = LocalContext.current  // Obtain the context here

    val humidityData by viewModel.humidity.collectAsState()
    val soilData by viewModel.soil.collectAsState()
    val temperatureData by viewModel.temperature.collectAsState()
    val locationData by viewModel.location.collectAsState()

    // Call the MonitorAndNotify function here
    MonitorAndNotify(context = context, humidity = humidityData, temperature = temperatureData)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp)
        ) {
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
                modifier = Modifier.padding(bottom = 20.dp)
            )

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
                        dataIcon = R.drawable.temperature,
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

        Box(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
        ) {
            LogoutButton(onLogoutClick = onLogoutClick)
        }
    }
}


@Preview
@Composable
fun PlantMonitoringUIPreview() {
    PlantMonitoringUI(viewModel = SensorDataViewModel(), onLogoutClick = {})
}