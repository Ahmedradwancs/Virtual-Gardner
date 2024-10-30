package com.example.virtualgardner.ui.screens

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
import com.example.virtualgardner.R
import com.example.virtualgardner.ui.components.LogoutButton
import com.example.virtualgardner.ui.theme.VirtualGardnerTheme
import com.example.virtualgardner.ui.theme.gradient
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomePageScreen(
    onMoistureClick: () -> Unit,
    onSmellDataClick: () -> Unit,
    onEnvironmentalClick: () -> Unit,
    onLocationClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    VirtualGardnerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        ) {
            HomePageContent(
                onMoistureClick = onMoistureClick,
                onSmellDataClick = onSmellDataClick,
                onEnvironmentalClick = onEnvironmentalClick,
                onLocationClick = onLocationClick,
                onLogoutClick = onLogoutClick
            )
        }
    }
}
@Composable
fun HomePageContent(
    onMoistureClick: () -> Unit,
    onSmellDataClick: () -> Unit,
    onEnvironmentalClick: () -> Unit,
    onLocationClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Row for User Dashboard and Logout Button, adjusted for alignment
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "User Dashboard",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4C4C4C)
            )

            Spacer(modifier = Modifier.weight(1f)) // Pushes LogoutButton to the right

            LogoutButton(onLogoutClick = onLogoutClick)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.smart),  // Replace with your image
            contentDescription = "Dashboard Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(150.dp)
        )

        // Add extra padding between the image and "Welcome to Virtual Gardener"
        Spacer(modifier = Modifier.height(16.dp)) // Adjust padding as needed

        Text(
            text = "Welcome to Virtual Gardener",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF585D66),
            modifier = Modifier.padding(top = 8.dp)
        )

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

        Text(
            text = "Ready to nurture your garden today? Stay updated with real-time sensor data and weather alerts to keep your plants thriving.",
            fontSize = 16.sp,
            color = Color(0xFFF5F5DC),
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        StatusSection(
            title = "Smell Detection",
            imageResId = R.drawable.smell,
            onClick = onSmellDataClick
        )

        StatusSection(
            title = "Environmental Status",
            imageResId = R.drawable.temp,
            onClick = onEnvironmentalClick
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
                .size(64.dp)
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
