package com.example.virtualgardner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class MoistureStatusActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoistureStatusPage()
        }
    }

    @Composable
    fun MoistureStatusPage() {
        Text(text = "Welcome to the Moisture Status Page")
    }
}
