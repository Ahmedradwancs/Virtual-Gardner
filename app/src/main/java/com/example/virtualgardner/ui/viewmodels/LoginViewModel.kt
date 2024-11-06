// viewmodels/LoginViewModel

package com.example.virtualgardner.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun validateLogin(): Boolean {
        return email.isNotBlank() && password.isNotBlank()
    }

    // Firebase login process
    fun loginWithEmail(auth: FirebaseAuth, onResult: (Boolean, String?) -> Unit) {
        if (!validateLogin()) {
            onResult(false, "Please fill in both fields.")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    // Firebase sign-out process
    fun logout(auth: FirebaseAuth, onLogoutSuccess: () -> Unit) {
        auth.signOut() // This signs out the current user from Firebase
        onLogoutSuccess() // After signing out, call this to navigate to login
    }

}
