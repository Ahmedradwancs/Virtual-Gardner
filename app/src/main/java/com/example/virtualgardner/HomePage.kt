package com.example.virtualgardner



import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.virtualgardner.R
import com.example.virtualgardner.ui.theme.VirtualGardnerTheme

@Composable
fun HomePage() {
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: User Dashboard
            Text(
                text = "User Dashboard",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            // Image
            Image(
                painter = painterResource(id = R.drawable.your_image),  // Replace with your actual image resource
                contentDescription = "Dashboard Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // Welcome text
            Text(
                text = "Welcome to Virtual Gardener",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Date and time
            Text(
                text = "Today 14 Oct 2024 13:00 pm",
                fontSize = 18.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Additional Information text
            Text(
                text = "Ready to nurture your garden today? Stay updated with real-time sensor data and weather alerts to keep your plants thriving.",
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )

            // Statuses/Buttons (Moisture, Smell, etc.)
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusButton("Moisture Status")
                StatusButton("Smell Data")
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                StatusButton("Environmental Status")
                StatusButton("Location Status")
            }
        }
    }
}

@Composable
fun StatusButton(text: String) {
    Button(
        onClick = { /* TODO: Handle click */ },
        shape = CircleShape,
        modifier = Modifier.size(100.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)  // Customize colors
    ) {
        Text(text = text, fontSize = 14.sp, color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    VirtualGardnerTheme {
        HomePage()
    }
}
