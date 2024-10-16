package com.example.virtualgardner.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.virtualgardner.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpViewModel : ViewModel() {
    var fullName by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set
    var phoneNumber by mutableStateOf("")
        private set
    var gardenType by mutableStateOf("")
        private set

    // Methods to update each field
    fun onFullNameChange(newName: String) { fullName = newName }
    fun onEmailChange(newEmail: String) { email = newEmail }
    fun onPasswordChange(newPassword: String) { password = newPassword }
    fun onConfirmPasswordChange(newConfirmPassword: String) { confirmPassword = newConfirmPassword }
    fun onPhoneNumberChange(newPhoneNumber: String) { phoneNumber = newPhoneNumber }
    fun onGardenTypeChange(newGardenType: String) { gardenType = newGardenType }

    // Validate user input
    fun validateSignUp(): Boolean {
        return fullName.isNotBlank()
                && email.isNotBlank()
                && password.isNotBlank()
                && confirmPassword.isNotBlank()
                && phoneNumber.isNotBlank()
                && gardenType.isNotBlank()
                && password == confirmPassword
    }

    // Function to create a User object after validation
    fun createUser(): User? {
        return if (validateSignUp()) {
            User(
                fullName = fullName,
                email = email,
                phoneNumber = phoneNumber,
                gardenType = gardenType
            )
        } else {
            null // Return null if validation fails
        }
    }

    // Firebase sign-up process with email and password
    fun signUpWithEmail(auth: FirebaseAuth, onResult: (Boolean, String?) -> Unit) {
        if (!validateSignUp()) {
            onResult(false, "Please fill in all fields correctly.")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = createUser()
                    user?.let {
                        saveUserToFirestore(it, auth.currentUser!!.uid, onResult)
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    // Function to save the user data to Firestore
    private fun saveUserToFirestore(user: User, uid: String, onResult: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(uid).set(user)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
            }
    }
}
