package com.example.virtualgardner.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.virtualgardner.ui.screens.LoginScreen
import com.example.virtualgardner.ui.screens.RegisterScreen
import com.example.virtualgardner.ui.screens.WelcomeScreen
import com.example.virtualgardner.ui.screens.HomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MyAppNavHost(
    startDestination: String,
    auth: FirebaseAuth
) {
    val navController = rememberNavController()

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
                onLoginClick = { navController.navigate("home"){popUpTo("login"){
                    inclusive = true
                }} },
                onForgotPasswordClick = { /* Handle forgot password */ },
                onSignUpClick = { navController.navigate("register")}

            )
        }
        composable("register") {
            RegisterScreen(
                onSignUpClick = { navController.navigate("home") },
                auth = auth
            )
        }

        composable("home") {
            HomeScreen(
                auth = auth,
                navController = navController
            )
        }
    }
}
