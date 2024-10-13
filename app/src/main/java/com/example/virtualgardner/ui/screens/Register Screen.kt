package com.example.virtualgardner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualgardner.R
import com.example.virtualgardner.ui.viewmodels.SignUpViewModel

@Composable
fun RegisterScreen(
    signUpViewModel: SignUpViewModel = viewModel(),
    onSignUpClick: () -> Unit
) {
    val fullName = signUpViewModel.fullName
    val email = signUpViewModel.email
    val password = signUpViewModel.password

    val backgroundImage = painterResource(id = R.drawable.register_1)
    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = backgroundImage,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
    }
}

@Preview
@Composable
fun RegisterScreenPreview(){
    RegisterScreen(){

    }
}
