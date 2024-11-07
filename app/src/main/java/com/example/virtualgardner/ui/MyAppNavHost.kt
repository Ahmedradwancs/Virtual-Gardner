package com.example.virtualgardner.ui

import PlantMonitoringUI
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.virtualgardner.ui.screens.HomePageScreen
import com.example.virtualgardner.ui.screens.LocationStatusScreen
import com.example.virtualgardner.ui.screens.MoistureStatusScreen
import com.example.virtualgardner.ui.screens.SmellDataScreen
import com.example.virtualgardner.ui.screens.LoginScreen
import com.example.virtualgardner.ui.screens.RegisterScreen
import com.example.virtualgardner.ui.screens.WelcomeScreen
import com.google.firebase.auth.FirebaseAuth



@Composable
fun MyAppNavHost(
    startDestination: String,
    auth: FirebaseAuth
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
                onLoginClick = { navController.navigate("home"){
                    popUpTo("welcome"){
                    inclusive = true
                }} },
                onForgotPasswordClick = { /* Handle forgot password */ },
                onSignUpClick = { navController.navigate("register")}

            )
        }
        composable("register") {
            RegisterScreen(
                onSignUpClick = { navController.navigate("home"){
                    popUpTo("welcome"){
                        inclusive = true
                    }
                } },
                auth = auth
            )
        }

        composable("home") {
            HomePageScreen(
                onMoistureClick = { navController.navigate("moisture") },
                onSmellDataClick = { navController.navigate("smell") },
                onEnvironmentalClick = { navController.navigate("environmental") },
                onLocationClick = { navController.navigate("location") },
                onLogoutClick = onLogoutClick
            )
        }

        composable("smell") {
            SmellDataScreen(onLogoutClick = onLogoutClick)
        }

        composable("environmental") {
            PlantMonitoringUI(onLogoutClick = onLogoutClick)  // Use this function to show environmental sensor data
        }

    }
}
