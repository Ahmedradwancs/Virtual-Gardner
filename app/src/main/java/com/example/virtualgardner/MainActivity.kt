package com.example.virtualgardner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.virtualgardner.ui.MyAppNavHost
import com.example.virtualgardner.ui.screens.SplashScreen
import com.example.virtualgardner.ui.theme.VirtualGardnerTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val currentUser = auth.currentUser

        setContent {
            VirtualGardnerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // State to manage splash screen visibility
                    var isSplashVisible by remember { mutableStateOf(true) }

                    // Show splash screen
                    if (isSplashVisible) {
                        SplashScreen {
                            isSplashVisible = false // Hide splash screen when it's done
                        }
                    } else {
                        // After the splash screen, navigate to MyAppNavHost
                        MyAppNavHost(
                            startDestination = if (auth.currentUser != null) "home" else "welcome",
                            auth = auth
                        )
                    }
                }
            }
        }
    }
}
