package com.example.virtualgardner

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virtualgardner.ui.theme.VirtualGardnerTheme
import com.example.virtualgardner.ui.theme.gradient
import java.text.SimpleDateFormat
import java.util.*

class HomePage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VirtualGardnerTheme {
                HomePageContent(
                    onMoistureClick = {
                        startActivity(Intent(this, MoistureStatusActivity::class.java))
                    },
                    onSmellDataClick = {
                        startActivity(Intent(this, SmellDataActivity::class.java))
                    },
                    onEnvironmentalClick = {
                        startActivity(Intent(this, EnvironmentalStatusActivity::class.java))
                    },
                    onLocationClick = {
                        startActivity(Intent(this, LocationStatusActivity::class.java))
                    }
                )
            }
        }
    }

    @Composable
    fun HomePageContent(
        onMoistureClick: () -> Unit,
        onSmellDataClick: () -> Unit,
        onEnvironmentalClick: () -> Unit,
        onLocationClick: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(12.dp)
                .background(gradient),  // Apply the gradient as the background
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: User Dashboard
            Text(
                text = "User Dashboard",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
                color = Color(0xFF4C4C4C)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Image (Main Dashboard Image)
            Image(
                painter = painterResource(id = R.drawable.smart),  // Replace with your image
                contentDescription = "Dashboard Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(150.dp)
            )

            // Welcome text
            Text(
                text = "Welcome to Virtual Gardener",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF585D66),
                modifier = Modifier.padding(top = 8.dp)
            )

            // Date and Time
            val currentDate = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault()).format(Date())
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Today: $currentDate",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFFF5F5DC),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Additional information text
            Text(
                text = "Ready to nurture your garden today? Stay updated with real-time sensor data and weather alerts to keep your plants thriving.",
                fontSize = 16.sp,
                color = Color(0xFFF5F5DC),
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Status Sections (Moisture, Smell, Environmental, Location)
            StatusSection(
                title = "Moisture Status",
                imageResId = R.drawable.icon2,  // Replace with your actual icon (uploaded icon1)
                onClick = onMoistureClick
            )
            StatusSection(
                title = "Smell Detection",
                imageResId = R.drawable.smell,  // Replace with your actual icon (uploaded icon2)
                onClick = onSmellDataClick
            )
            StatusSection(
                title = "Environmental Status",
                imageResId = R.drawable.temp,  // Replace with your actual icon (uploaded icon3)
                onClick = onEnvironmentalClick
            )
            StatusSection(
                title = "Location Status",
                imageResId = R.drawable.location,  // Replace with your actual icon (uploaded icon4)
                onClick = onLocationClick
            )
        }
    }

    @Composable
    fun StatusSection(title: String, imageResId: Int, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = title,
                contentScale = ContentScale.Fit,  // Make sure the image is scaled properly
                modifier = Modifier
                    .size(64.dp)   // Adjust to make all icons 128x128 dp (adjust as needed)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF5F5DC)
            )
        }
    }
}
