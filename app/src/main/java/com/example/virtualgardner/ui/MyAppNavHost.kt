package com.example.virtualgardner.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.virtualgardner.ui.screens.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MyAppNavHost(
    startDestination: String,
    auth: FirebaseAuth,
    location: String
) {
    val navController = rememberNavController()

    // Define the logout function
    val onLogoutClick: () -> Unit = {
        auth.signOut()
        navController.navigate("welcome") {
            popUpTo("home") { inclusive = true } // Ensures all screens are removed from the back stack
        }
    }

    NavHost(navController, startDestination = startDestination) {
        composable("welcome") {
            WelcomeScreen(
                onSignInClick = { navController.navigate("login") },
                onSignUpClick = { navController.navigate("register") }
            )
        }
        composable("login") {
            LoginScreen(
                auth = auth,
                onLoginClick = {
                    navController.navigate("home") {
                        popUpTo("welcome") {
                            inclusive = true
                        }
                    }
                },
                onForgotPasswordClick = { /* Handle forgot password */ },
                onSignUpClick = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                onSignUpClick = {
                    navController.navigate("home") {
                        popUpTo("welcome") {
                            inclusive = true
                        }
                    }
                },
                auth = auth
            )
        }

        // Only the main dashboard (HomePageScreen) has the Logout button
        composable("home") {
            HomePageScreen(
                onMoistureClick = { navController.navigate("moisture") },
                onSmellDataClick = { navController.navigate("smell") },
                onEnvironmentalClick = { navController.navigate("environmental") },
                onLocationClick = { navController.navigate("location") },
                onLogoutClick = onLogoutClick
            )
        }

        // Remove onLogoutClick from other screens
        composable("moisture") {
            MoistureStatusScreen()
        }

        composable("smell") {
            SmellDataScreen(onLogoutClick = onLogoutClick)
        }

        composable("location") {
            LocationStatusScreen()
        }

        composable("environmental") {
            PlantMonitoringUI(
                location = location
            )
        }
    }
}
