package com.example.discordcloneapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val channel: String,
    val user: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)