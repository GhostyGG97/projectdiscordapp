package com.example.discordcloneapp.data

import com.example.discordcloneapp.dao.ChatMessageDao
import com.example.discordcloneapp.models.PendingMessage
import com.example.discordcloneapp.network.WebSocketManager
import com.example.discordcloneapp.dao.PendingMessageDao
import com.example.discordcloneapp.models.ChatMessageEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessageRepository(
    private val pendingDao: PendingMessageDao,
    private val chatDao: ChatMessageDao
) {

    init {
        WebSocketManager.init(pendingDao)
    }
    fun saveToRoom(channel: String, user: String, content: String) {
        CoroutineScope(Dispatchers.IO).launch {
            println("🔽 Enviando a Room → [$channel] $user: $content")
            chatDao.insertMessage(
                ChatMessageEntity(channel = channel, user = user, content = content)
            )
            println("✅ Mensaje guardado correctamente en Room")
        }
    }
    fun connect(onMessage: (ChatMessageEntity) -> Unit) {
        WebSocketManager.connect { raw ->
            val match = Regex("""^\[(.+?)]\s(.+?)\|(.+)$""").find(raw)
            val (channel, user, content) = match?.destructured ?: return@connect

            val entity = ChatMessageEntity(
                channel = channel,
                user = user,
                content = content
            )

            // Guardar en Room
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    println("⏳ Intentando guardar mensaje en Room")
                    chatDao.insertMessage(entity)
                    println("✅ Mensaje guardado en Room: ${entity.content}")
                } catch (e: Exception) {
                    println("❌ Error guardando en Room: ${e.message}")
                }
            }
        }
    }

    fun sendMessage(content: String, userId: String, channel: String) {
        WebSocketManager.send(content, userId, channel)
    }

    fun getHistory(channel: String, onLoaded: (List<ChatMessageEntity>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = chatDao.getMessagesForChannel(channel)
            withContext(Dispatchers.Main) {
                onLoaded(list)
            }
        }
    }

    fun disconnect() {
        WebSocketManager.disconnect()
    }

    fun clearPending() {
        CoroutineScope(Dispatchers.IO).launch {
            pendingDao.clearAll()
        }
    }
}