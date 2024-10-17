package com.example.virtualgardner

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

class HomePage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Virtual Gardner",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            val currentDate = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault()).format(Date())
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Today: $currentDate",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card for "Moisture Status"
            StatusSection(
                title = "Moisture Status",
                imageResId = R.drawable.icon1,
                onClick = onMoistureClick
            )

            // Card for "Smell Data"
            StatusSection(
                title = "Smell Data",
                imageResId = R.drawable.smart,
                onClick = onSmellDataClick
            )

            // Card for "Environmental Status"
            StatusSection(
                title = "Environmental Status",
                imageResId = R.drawable.icon3,
                onClick = onEnvironmentalClick
            )

            // Card for "Location Status"
            StatusSection(
                title = "Location Status",
                imageResId = R.drawable.icon4,
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
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, fontSize = 18.sp)
        }
    }
}
