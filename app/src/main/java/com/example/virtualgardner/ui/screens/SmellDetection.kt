package com.example.virtualgardner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virtualgardner.R
import com.example.virtualgardner.ui.theme.gradient

@Composable
fun SmellDetectionScreen() {
    SmellDetectionPage()
}
@Composable
fun SmellDetectionPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally  // Centering the content
    ) {
        // Header: Smell Sensor Data
        Text(
            text = "Smell Sensor Data",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp),
            color = Color(0xFFF5F5DC),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Real Time Monitoring of Plant Emissions",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top= 24.dp, bottom = 16.dp),
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        // Image for plant emissions icon
        Image(
            painter = painterResource(id = R.drawable.emission_icon), // Update with correct icon
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Preliminary Inspection Section
        Text(
            text = "Preliminary Inspection",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp),
            color = Color.Black,
            textAlign = TextAlign.Center  // Centering text
        )

        // Hydrogen Sulfide Info with Icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.gas1), // Replace with the icon for H2S
                contentDescription = "H2S Icon",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Hydrogen Sulphide: 0.05%",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF585D66)
            )
        }
        Text(
            text = "High levels of H₂S can be toxic, causing stunted growth in plants.",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp),
            color = Color.White,
            textAlign = TextAlign.Center
        )

        // Ammonia Info with Icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.gas2), // Replace with the icon for Ammonia
                contentDescription = "Ammonia Icon",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Ammonia Level: 0.02%",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF585D66)
            )
        }
        Text(
            text = "Bacteria and fungi can produce ammonia which led to root rot.",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp),
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Corrective Measures Section
        Text(
            text = "Apply Corrective Measures",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp),
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        // Corrective actions
        Text(
            text = "• Optimize Hydrogen Sulphide levels to fuel robust plant development.",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp),
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = "• Ensure proper drainage and apply antifungals to prevent root rot.",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp),
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

