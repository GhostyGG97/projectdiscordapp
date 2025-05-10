package com.example.discordcloneapp.viewmodel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discordcloneapp.data.ChannelRepository
import com.example.discordcloneapp.data.MessageRepository
import com.example.discordcloneapp.models.Channel
import com.example.discordcloneapp.models.ChatMessage
import com.example.discordcloneapp.network.WebSocketManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content


class ChatViewModel( private val repository: MessageRepository) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var channels by mutableStateOf(listOf<Channel>())
        private set

    var selectedChannel by mutableStateOf<Channel?>(null)
        private set

    var currentChannel by mutableStateOf("general")
        private set

    val isConnected = WebSocketManager.isConnectedFlow.asStateFlow()
    private var channelMessages = mutableStateMapOf<String, List<ChatMessage>>()

    val messages: List<ChatMessage>
        get() = channelMessages[currentChannel] ?: emptyList()

    // 🔄 Escuchar mensajes del WebSocket
    fun startListening() {
        println("🔔 Iniciando escucha en WebSocket")
        repository.connect { entity ->
            addMessage("[${entity.channel}] ${entity.user}|${entity.content}")
        }
        repository.connect { entity ->
            val channel = entity.channel
            val current = channelMessages[channel] ?: emptyList()

            // Elimina duplicado si estaba pendiente
            val updated = current.filterNot {
                it.user == entity.user && it.content == entity.content && it.isPending
            } + ChatMessage(user = entity.user, content = entity.content)

            channelMessages[channel] = updated
        }
    }

    // 🔁 Cambiar de canal
    fun selectChannel(channel: String) {
        currentChannel = channel
        println("📡 Seleccionado canal: $channel")
        repository.getHistory(channel) { history ->
            println("🧾 Cargados ${history.size} mensajes para $channel")
            channelMessages[channel] = history.map {
                ChatMessage(user = it.user, content = it.content)
            }
        }
        //viewModelScope.launch {
            //repository.getHistory(channel) { history ->
                //println("🧾 Cargados ${history.size} mensajes para $channel")
                //channelMessages[channel] = history.map {
                    //ChatMessage(user = it.user, content = it.content)
                //}
            //}
        //}

        repository.sendMessage("JOIN", "system", channel)
    }

    // 📨 Enviar mensaje al servidor o guardarlo si está offline
    fun sendMessage(userId: String, content: String) {
        val channel = currentChannel

        repository.sendMessage(content, userId, channel)

        val current = channelMessages[channel] ?: emptyList()
        channelMessages[channel] = current + ChatMessage(
            user = userId,
            content = content,
            isPending = true
        )
    }

    // 🔧 Agregar mensaje manualmente desde WebSocket (formato raw)
    fun addMessage(raw: String) {
        println("📩 ChatViewModel.addMessage() llamado con: $raw")

        val regex = Regex("""^\[(.+?)]\s(.+?)\|(.+)$""")
        val match = regex.find(raw)
        if (match == null) {
            println("❌ No coincide con el formato esperado")
            return
        }

        val (channel, user, content) = match.destructured
        println("📦 Canal=$channel, Usuario=$user, Contenido=$content")

        val updated = (channelMessages[channel] ?: emptyList()) + ChatMessage(user, content)
        channelMessages[channel] = updated

        // Guardar en Room
        viewModelScope.launch(Dispatchers.IO) {
            println("💾 Insertando mensaje en Room desde addMessage")
            repository.saveToRoom(channel, user, content)
        }
    }

    fun addMessage(channel: String, user: String, content: String) {
        val updated = (channelMessages[channel] ?: emptyList()) + ChatMessage(user, content)
        channelMessages[channel] = updated

        viewModelScope.launch(Dispatchers.IO) {
            repository.saveToRoom(channel, user, content)
        }
    }

    fun listenToChannels() {
        db.collection("channels")
            .orderBy("name")
            .addSnapshotListener { snapshot, _ ->
                channels = snapshot?.documents?.map {
                    Channel(id = it.id, name = it.getString("name") ?: "")
                } ?: emptyList()
            }
    }

    fun addChannel(name: String, userId: String) {
        val data = hashMapOf(
            "name" to name,
            "createdBy" to userId,
            "createdAt" to System.currentTimeMillis()
        )
        db.collection("channels")
            .add(data)
    }

    override fun onCleared() {
        repository.disconnect()
        super.onCleared()
    }
}
