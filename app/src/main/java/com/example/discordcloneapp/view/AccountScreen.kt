package com.example.discordcloneapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.discordcloneapp.viewmodel.LoginViewModel

@Composable
fun AccountScreen(
    loginViewModel: LoginViewModel,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Mi cuenta")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            loginViewModel.logout()
            onLogout()
        }) {
            Text("Cerrar sesión")
        }
    }
}