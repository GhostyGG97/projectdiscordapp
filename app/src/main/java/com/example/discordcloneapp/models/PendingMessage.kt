package com.example.discordcloneapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_messages")
data class PendingMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val channel: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)