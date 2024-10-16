package com.example.virtualgardner.ui.screens

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualgardner.ui.theme.BtnColor
import com.example.virtualgardner.ui.theme.gradient
import com.example.virtualgardner.ui.theme.OffWhite
import com.example.virtualgardner.ui.viewmodels.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterScreen(
    signUpViewModel: SignUpViewModel = viewModel(),
    onSignUpClick: () -> Unit,
    auth: FirebaseAuth, // Pass FirebaseAuth instance
) {
    var mExpanded by remember { mutableStateOf(false) } // for DropdownMenu state
    var passwordVisible by remember { mutableStateOf(false) } // visibility state for password
    var confirmPasswordVisible by remember { mutableStateOf(false) } // visibility state for confirm password
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) } // for DropdownMenu width
    val gardenTypes = listOf("Indoor", "Outdoor", "Green House") // garden types list
    var errorMessage by remember { mutableStateOf("") } // To store any error messages

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Create header text
            item {
                Text(
                    text = "User Profile",
                    style = MaterialTheme.typography.displayMedium,
                    color = OffWhite,
                    modifier = Modifier.padding(12.dp)
                )
            }
            item { UserTextField(signUpViewModel.fullName, { signUpViewModel.onFullNameChange(it) }, "Full Name", Icons.Filled.Person) }
            item { UserTextField(signUpViewModel.email, { signUpViewModel.onEmailChange(it) }, "Email", Icons.Filled.Email, keyboardType = KeyboardType.Email) }
            item {
                TextField(
                    value = signUpViewModel.gardenType,
                    onValueChange = { signUpViewModel.onGardenTypeChange(it) },
                    label = { Text("Garden Type") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            mTextFieldSize = coordinates.size.toSize()
                        },
                    readOnly = true,
                    trailingIcon = {
                        Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null, modifier = Modifier.clickable { mExpanded = true })
                    }
                )
            }
            item { UserTextField(signUpViewModel.phoneNumber, { signUpViewModel.onPhoneNumberChange(it) }, "Phone Number", Icons.Filled.Phone, keyboardType = KeyboardType.Phone) }
            item { PasswordTextField(signUpViewModel.password, ImeAction.Next,  { signUpViewModel.onPasswordChange(it) }, passwordVisible) { passwordVisible = !passwordVisible }}
            item { PasswordTextField(signUpViewModel.confirmPassword, ImeAction.Done, { signUpViewModel.onConfirmPasswordChange(it) }, confirmPasswordVisible) { confirmPasswordVisible = !confirmPasswordVisible }}
            item {
                val context = LocalContext.current
                Button(
                    onClick = {
                        if (signUpViewModel.validateSignUp()) {
                            auth.createUserWithEmailAndPassword(signUpViewModel.email, signUpViewModel.password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        onSignUpClick()
                                    } else {
                                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                        errorMessage = task.exception?.message ?: "An error occurred"
                                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            errorMessage = "Please fill in all fields correctly."
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(BtnColor)
                ) {
                    Text("Sign Up")
                }
            }
        }

        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
        ) {
            gardenTypes.forEach { gardenType ->
                DropdownMenuItem(
                    text = { Text(gardenType) },
                    onClick = {
                        signUpViewModel.onGardenTypeChange(gardenType)
                        mExpanded = false // Close the dropdown after selection
                    }
                )
            }
        }
    }
}

@Composable
fun UserTextField(value: String, onValueChange: (String) -> Unit, label: String, icon: ImageVector, keyboardType: KeyboardType = KeyboardType.Text) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next,

        ),
        trailingIcon = {
            Icon(imageVector = icon, contentDescription = null)
        }
    )
}

@Composable
fun PasswordTextField(value: String, keyboaardBtn : ImeAction, onValueChange: (String) -> Unit, isVisible: Boolean, onVisibilityToggle: () -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Password") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = keyboaardBtn
        ),
        trailingIcon = {
            IconButton(onClick = onVisibilityToggle) {
                Icon(
                    imageVector = if (isVisible) Icons.Filled.FavoriteBorder else Icons.Filled.Favorite,
                    contentDescription = if (isVisible) "Hide password" else "Show password"
                )
            }
        }
    )
}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(onSignUpClick = {}, auth = FirebaseAuth.getInstance())
}
