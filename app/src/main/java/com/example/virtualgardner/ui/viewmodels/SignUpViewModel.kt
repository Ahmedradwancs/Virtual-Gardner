package com.example.virtualgardner.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {
    var fullName by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var gardenType by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set
    var phoneNumber by mutableStateOf("")
        private set


    fun onFullNameChange(newName: String) {
        fullName = newName
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
    }

    fun onPhoneNumberChange(newPhoneNumber: String) {
        phoneNumber = newPhoneNumber
    }

    fun onGardenTypeChange(newGardenType: String){
        gardenType = newGardenType
    }

    fun validateSignUp(): Boolean {
        return fullName.isNotBlank()
                && email.isNotBlank()
                && password.isNotBlank()
                && confirmPassword.isNotBlank()
                && phoneNumber.isNotBlank()
                && gardenType.isNotBlank()
                && password == confirmPassword
    }
}
