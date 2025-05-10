package com.example.discordcloneapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.discordcloneapp.network.WebSocketManager
import androidx.compose.foundation.lazy.items
import com.example.discordcloneapp.viewmodel.ChatViewModel

@Composable
fun ChatRoomScreen(viewModel: ChatViewModel, userId: String) {
    var message by remember { mutableStateOf("") }
    val messages = viewModel.messages
    val channel = viewModel.currentChannel
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF36393F))
    ) {
        // Encabezado del canal
        TopAppBar(
            backgroundColor = Color(0xFF2F3136),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = channel,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        )

        // Dentro del Column principal
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            items(items = messages) { message ->
                MessageItem(
                    user = message.user,
                    content = message.content
                )
            }
        }

        // Barra de entrada de mensaje
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF40444B))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White, RoundedCornerShape(8.dp)),
                placeholder = { Text("Escribe un mensaje...") },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    textColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            //Boton de enviar por socket
            IconButton(onClick = {
                if (message.isNotBlank()) {
                    WebSocketManager.send(message, userId, channel)
                    viewModel.addMessage(channel, userId, message) // 👈 AGREGA ESTA LÍNEA
                    message = ""
                }
            }) {
                Icon(Icons.Default.Send, contentDescription = "Enviar", tint = Color.White)
            }
        }
    }
}


@Composable
fun MessageItem(user: String, content: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFF7289DA), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.first().uppercase(),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = user,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = content,
                color = Color.LightGray,
                fontSize = 14.sp
            )
        }
    }
}