package com.example.virtualgardner.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val GreenColor = Color(0xFF4CAF50)
val DarkGreenColor = Color(0xFF388E3C)
val LightGrayColor = Color(0xFFE0E0E0)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val OffWhite = Color(0xFFF5F5DC)
val OffBlack = Color(0xFF585D66)
val BtnColor = Color(0XFF3B2314)

// Create a linear gradient background similar to the provided CSS
val gradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF333333).copy(alpha = 0.5f), // rgba(51, 51, 51, 0.5)
        Color(0xFF006400).copy(alpha = 0.4f)  // rgba(0, 100, 0, 0.4)
    ),
    start = Offset(0f, 0f),
    end = Offset(0f, Float.POSITIVE_INFINITY) // Simulates the 180-degree angle
)