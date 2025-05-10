package com.example.discordcloneapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.discordcloneapp.network.WebSocketManager
import com.example.discordcloneapp.viewmodel.ChatViewModel
import com.example.discordcloneapp.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import android.util.Log

@Composable
fun HomeScreen(
    loginViewModel: LoginViewModel,
    onLogout: () -> Unit,
    chatViewModel: ChatViewModel
) {
    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf(0) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"
    val user = FirebaseAuth.getInstance().currentUser


    LaunchedEffect(Unit) {
        WebSocketManager.attachViewModel(chatViewModel)
        WebSocketManager.connect { incomingMessage ->
            chatViewModel.addMessage(incomingMessage)
        }
        chatViewModel.startListening()
    }


    // 🔌 Desconectarse del WebSocket al salir del HomeScreen
    DisposableEffect(Unit) {
        onDispose {
            WebSocketManager.disconnect()
        }
    }
    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color(0xFF2F3136),
                contentColor = Color.White
            ) {
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Chats"
                        )
                    },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    selectedContentColor = Color(0xFF5865F2), // Azul Discord
                    unselectedContentColor = Color.Gray
                )

                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones"
                        )
                    },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    selectedContentColor = Color(0xFF5865F2),
                    unselectedContentColor = Color.Gray
                )

                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Cuenta"
                        )
                    },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    selectedContentColor = Color(0xFF5865F2),
                    unselectedContentColor = Color.Gray
                )
            }
        },
        backgroundColor = Color(0xFF36393F) // Fondo general Discord
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (selectedTab) {
                0 -> ChatScreen(viewModel = chatViewModel, userId = userId)
                1 -> NotificationsScreen()
                2 -> AccountScreen(loginViewModel = loginViewModel, onLogout = onLogout)
            }
        }
    }
}

