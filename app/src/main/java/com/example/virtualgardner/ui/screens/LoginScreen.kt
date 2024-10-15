package com.example.virtualgardner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualgardner.R
import com.example.virtualgardner.ui.theme.BtnColor
import com.example.virtualgardner.ui.theme.gradient
import com.example.virtualgardner.ui.theme.OffWhite
import com.example.virtualgardner.ui.theme.OffBlack
import com.example.virtualgardner.ui.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {


        // Centered Login Form
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(){
                Text("Welcome Back", color = OffWhite, style = MaterialTheme.typography.displayMedium)
                Text(
                    "Login to your account",
                    style = MaterialTheme.typography.titleLarge,
                    color = OffBlack
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            Column (){

                // Email field
                TextField(
                    value = loginViewModel.email,
                    onValueChange = { loginViewModel.onEmailChange(it) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    trailingIcon =  {
                        Icon(imageVector = Icons.Filled.Email, contentDescription = null)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field with visibility toggle
                TextField(
                    value = loginViewModel.password,
                    onValueChange = { loginViewModel.onPasswordChange(it) },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) Icons.Filled.Lock else Icons.Filled.Lock
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = onForgotPasswordClick, modifier = Modifier.align(Alignment.End)) {
                    Text("Forgot Password?", color = OffBlack)
                }

            }

            //Spacer(modifier = Modifier.height(1.dp))

            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                // Login Button
                Button(
                    onClick = {
                        if (loginViewModel.validateLogin()) {
                            onLoginClick()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(BtnColor)
                ) {
                    Text("Login", color = OffWhite)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row (modifier = Modifier, horizontalArrangement = Arrangement.Center)
                {

                    Text("Don't have an account? ", color = OffWhite, modifier = Modifier.padding(top = 15.dp))
                    TextButton(onClick = onSignUpClick) {
                        Text("Sign Up", color = OffBlack, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onLoginClick = {}, onForgotPasswordClick = {}, onSignUpClick = {})
}
