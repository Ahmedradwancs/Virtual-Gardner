package com.example.virtualgardner.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.example.virtualgardner.ui.viewmodels.LoginViewModel

@Composable
fun HomeScreen(
    auth: FirebaseAuth,
    navController: NavController
) {
    Text(text ="Home Screen")
    // create log out button
    Button(onClick = {
        LoginViewModel().logout(auth) {
            // navigate to login screen
            navController.navigate("login"){
                popUpTo("home"){
                    inclusive = true
                }
            }
        }

    }) {
        Text("Log Out")
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        auth = FirebaseAuth.getInstance(),
        navController = NavController(LocalContext.current)  // Dummy for preview purposes
    )
}

