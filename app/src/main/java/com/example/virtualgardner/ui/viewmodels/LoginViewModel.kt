package com.example.virtualgardner.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var fullName by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    fun onFullNameChange(newName: String) {
        fullName = newName
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun validateLogin(): Boolean {
        return fullName.isNotBlank() && password.isNotBlank()
    }
}
