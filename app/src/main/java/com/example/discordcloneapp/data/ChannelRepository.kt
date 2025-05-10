package com.example.discordcloneapp.data

import com.example.discordcloneapp.models.Channel
import com.google.firebase.firestore.FirebaseFirestore

class ChannelRepository {
    private val db = FirebaseFirestore.getInstance()
    private val channelsRef = db.collection("channels")

    fun listenToChannels(onChange: (List<Channel>) -> Unit) {
        channelsRef.orderBy("createdAt")
            .addSnapshotListener { snapshot, _ ->
                val list = snapshot?.documents?.mapNotNull {
                    it.toObject(Channel::class.java)?.copy(id = it.id)
                } ?: emptyList()
                onChange(list)
            }
    }

    fun createChannel(name: String, userId: String) {
        val data = hashMapOf(
            "name" to name,
            "createdBy" to userId,
            "createdAt" to System.currentTimeMillis()
        )
        channelsRef.add(data)
    }
}