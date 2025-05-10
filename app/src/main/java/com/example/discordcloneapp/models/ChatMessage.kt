package com.example.discordcloneapp.models

data class ChatMessage(
    val user: String,
    val content: String,
    val isPending: Boolean = false
)