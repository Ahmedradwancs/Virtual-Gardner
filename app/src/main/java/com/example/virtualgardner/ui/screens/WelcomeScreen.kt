package com.example.virtualgardner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virtualgardner.R

@Composable
fun WelcomeScreen(
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5DC))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF333333).copy(alpha = 0.5f),
                        Color(0xFF006400).copy(alpha = 0.4f)
                    )
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = "Join for Smart Gardening experience",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF5F5DC),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(70.dp)) // Fixed height for spacing

            Image(
                painter = painterResource(id = R.drawable.wlecome),
                contentDescription = "Welcome to Virtual Gardener",
                modifier = Modifier
                    .fillMaxWidth(0.8f) // 80% of the available width
                    .aspectRatio(1f) // Aspect ratio 1:1 for circle shape
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(35.dp)) // Fixed height for spacing

            Button(
                onClick = onSignInClick,
                modifier = Modifier
                    .fillMaxWidth(0.7f) // 70% of available width for buttons
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B2314).copy(alpha = 0.5f) // Button with transparency
                )
            ) {
                Text("Sign in", fontSize = 20.sp, color = Color(0xFFF5F5DC))
            }

            Spacer(modifier = Modifier.height(35.dp)) // Fixed height for spacing

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                ClickableText(
                    text = AnnotatedString("Create an account"),
                    onClick = { onSignUpClick() },
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color(0xFFF5F5DC),
                        textDecoration = TextDecoration.Underline
                    )
                )
            }

            Spacer(modifier = Modifier.height(70.dp)) // Fixed height for spacing
        }
    }
}

@Preview
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen({}, {})
}