// components/LogoutButton.kt

package com.example.virtualgardner.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.virtualgardner.ui.theme.BtnColor

@Composable
fun LogoutButton(onLogoutClick: () -> Unit) {
    Button(
        onClick = onLogoutClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = BtnColor
        ),
    ) {
        Text("Logout")
    }
}

@Preview
@Composable
fun PreviewLogoutButton() {
    LogoutButton(onLogoutClick = {})
}