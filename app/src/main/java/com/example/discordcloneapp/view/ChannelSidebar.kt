package com.example.discordcloneapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.discordcloneapp.viewmodel.ChatViewModel
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material.Icon

@Composable
fun ChannelSidebar(
    viewModel: ChatViewModel,
    userId: String,
    onSelectChannel: (String) -> Unit
) {
    var channels by remember { mutableStateOf(listOf<String>()) }
    var showDialog by remember { mutableStateOf(false) }
    var newChannel by remember { mutableStateOf("") }

    // Escuchar cambios en canales
    LaunchedEffect(true) {
        FirebaseFirestore.getInstance()
            .collection("channels")
            .addSnapshotListener { snapshot, _ ->
                channels = snapshot?.documents?.mapNotNull { it.getString("name") } ?: emptyList()
            }
    }

    Column(
        modifier = Modifier
            .width(72.dp)
            .fillMaxHeight()
            .background(Color(0xFF202225)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        channels.forEach { name ->
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp)
                    .background(Color.Gray, shape = CircleShape)
                    .clickable { onSelectChannel(name) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.take(1).uppercase(),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }

        // Botón "+"
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color(0xFF3BA55D), shape = CircleShape)
                .clickable { showDialog = true },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, contentDescription = "Nuevo canal", tint = Color.White)
        }
    }

    // Diálogo para agregar canal
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Crear nuevo canal") },
            text = {
                OutlinedTextField(
                    value = newChannel,
                    onValueChange = { newChannel = it },
                    label = { Text("Nombre del canal") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (newChannel.isNotBlank()) {
                        viewModel.addChannel(newChannel, userId)
                        newChannel = ""
                        showDialog = false
                    }
                }) {
                    Text("Crear")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}