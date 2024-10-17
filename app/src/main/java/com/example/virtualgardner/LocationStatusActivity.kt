package com.example.virtualgardner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class LocationStatusActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocationStatusPage()
        }
    }

    @Composable
    fun LocationStatusPage() {
        Text(text = "Welcome to the Location Status Page")
    }
}
