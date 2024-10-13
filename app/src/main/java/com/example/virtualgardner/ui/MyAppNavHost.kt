package com.example.virtualgardner.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.virtualgardner.ui.screens.LoginScreen
import com.example.virtualgardner.ui.screens.RegisterScreen
import com.example.virtualgardner.ui.screens.WelcomeScreen

@Composable
fun MyAppNavHost() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(
                onSignInClick = { navController.navigate("login") },
                onSignUpClick = { navController.navigate("register") }
            )
        }
        composable("login") {
            LoginScreen(
                onLoginClick = { navController.navigate("home") },
                onForgotPasswordClick = { /* Handle forgot password */ },
                onSignUpClick = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                onSignUpClick = { navController.navigate("login") }
            )
        }
    }
}
