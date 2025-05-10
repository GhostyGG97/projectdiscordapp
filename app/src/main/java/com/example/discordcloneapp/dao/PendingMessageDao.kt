package com.example.discordcloneapp.dao

import androidx.room.*
import com.example.discordcloneapp.models.PendingMessage

@Dao
interface PendingMessageDao {
    @Query("SELECT * FROM pending_messages ORDER BY timestamp ASC")
    suspend fun getAll(): List<PendingMessage>

    @Insert
    suspend fun insert(message: PendingMessage)

    @Delete
    suspend fun delete(message: PendingMessage)

    @Query("DELETE FROM pending_messages")
    suspend fun clearAll()
}