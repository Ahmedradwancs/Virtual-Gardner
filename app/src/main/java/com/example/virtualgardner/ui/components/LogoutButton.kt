// components/LogoutButton.kt

package com.example.virtualgardner.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.virtualgardner.R

@Composable
fun LogoutButton(onLogoutClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp) // Adjust size for a larger clickable area
            .clickable(onClick = onLogoutClick) // Use onClick parameter directly
    ) {
        Image(
            painter = painterResource(id = R.drawable.logouticon),
            contentDescription = "Logout",
            modifier = Modifier.size(32.dp)
        )
    }
}


@Preview
@Composable
fun PreviewLogoutButton() {
    LogoutButton(onLogoutClick = {})
}