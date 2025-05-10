package com.example.discordcloneapp.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.discordcloneapp.models.PendingMessage
import com.example.discordcloneapp.models.ChatMessageEntity
import com.example.discordcloneapp.dao.PendingMessageDao
import com.example.discordcloneapp.dao.ChatMessageDao

@Database(
    entities = [ChatMessageEntity::class, PendingMessage::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun pendingMessageDao(): PendingMessageDao
}