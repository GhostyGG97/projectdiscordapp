package com.example.discordcloneapp.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.ExperimentalEncodingApi
import android.util.Base64
import android.util.Log
import com.example.discordcloneapp.dao.PendingMessageDao
import com.example.discordcloneapp.models.PendingMessage
import com.example.discordcloneapp.viewmodel.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

object WebSocketManager {
    private var webSocket: WebSocket? = null
    private var isConnected = false
    private var dao: PendingMessageDao? = null
    private var fallbackViewModel: ChatViewModel? = null
    private val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS) // escucha indefinidamente
        .build()
    private const val SERVER_URL = "ws://10.0.2.2:8888/chat" // IP del servidor
    //Pending for messages
    private val pendingMessages = mutableListOf<Pair<String, String>>() // userId, message
    val isConnectedFlow = MutableStateFlow(false)
    private var currentChannel: String? = null

    fun init(pendingMessageDao: PendingMessageDao) {
        dao = pendingMessageDao
    }

    fun connect(onMessage: (String) -> Unit) {
        val request = Request.Builder().url(SERVER_URL).build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                isConnected = true
                isConnectedFlow.value = true

                CoroutineScope(Dispatchers.IO).launch {
                    dao?.getAll()?.forEach { msg ->
                        val formatted = "#${msg.channel}:${msg.userId}|${msg.content}"
                        webSocket?.send(formatted)
                        dao?.delete(msg)
                    }
                }
            }

            override fun onMessage(ws: WebSocket, text: String) {
                onMessage(text) // ✅ llamado al callback
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                isConnected = false
                isConnectedFlow.value = false
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                isConnected = false
                isConnectedFlow.value = false
            }
        })
    }

    fun attachViewModel(viewModel: ChatViewModel) {
        fallbackViewModel = viewModel
    }

    fun joinChannel(channelName: String) {
        currentChannel = channelName
        webSocket?.send("JOIN #$channelName")
    }

    fun send(message: String, userId: String, channel: String) {
        val formatted = "#$channel:$userId|$message"
        if (isConnected && webSocket != null) {
            webSocket?.send(formatted)
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                dao?.insert(
                    PendingMessage(
                        userId = userId,
                        channel = channel,
                        content = message
                    )
                )
                // Mostrarlo inmediatamente en la UI
                withContext(Dispatchers.Main) {
                    fallbackViewModel?.addMessage(channel, userId, message) // 👈
                }
            }
        }
    }

    fun disconnect() {
        webSocket?.close(1000, "Bye")
    }

}