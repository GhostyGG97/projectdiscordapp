package com.example.discordcloneapp.dao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.discordcloneapp.data.MessageRepository
import com.example.discordcloneapp.viewmodel.ChatViewModel

class ChatViewModelFactory(
    private val repository: MessageRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}