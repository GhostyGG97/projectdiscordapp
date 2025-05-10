package com.example.discordcloneapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.discordcloneapp.dao.AppDatabase
import com.example.discordcloneapp.dao.ChatViewModelFactory
import com.example.discordcloneapp.data.MessageRepository
import com.example.discordcloneapp.view.HomeScreen
import com.example.discordcloneapp.view.LoginScreen
import com.example.discordcloneapp.viewmodel.ChatViewModel
import com.example.discordcloneapp.viewmodel.LoginViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicia la base de datos de Room
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "discordclone.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        // Crea el repositorio usando el DAO
        val repository = MessageRepository(database.pendingMessageDao(),  chatDao = database.chatMessageDao())

        setContent {
            val loginViewModel: LoginViewModel = viewModel()
            var isLoggedIn by remember { mutableStateOf(loginViewModel.isUserLoggedIn()) }

            if (!isLoggedIn) {
                LoginScreen(
                    loginViewModel = loginViewModel,
                    onLoginSuccess = { isLoggedIn = true }
                )
            } else {
                val chatViewModel: ChatViewModel = viewModel(
                    factory = ChatViewModelFactory(repository)
                )
                HomeScreen(
                    loginViewModel = loginViewModel,
                    onLogout = {
                        loginViewModel.logout()
                        isLoggedIn = false
                    },
                    chatViewModel = chatViewModel
                )
            }
        }
    }
}