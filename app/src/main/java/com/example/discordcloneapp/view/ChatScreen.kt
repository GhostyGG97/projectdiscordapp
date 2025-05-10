package com.example.discordcloneapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import com.example.discordcloneapp.network.WebSocketManager
import com.example.discordcloneapp.viewmodel.ChatViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(viewModel: ChatViewModel, userId: String) {
    var selectedChannel by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        WebSocketManager.connect { incomingMessage ->
            viewModel.addMessage(incomingMessage)
        }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        ChannelSidebar(
            viewModel = viewModel,
            userId = userId,
            onSelectChannel = {
                selectedChannel = it
                viewModel.selectChannel(it) // ← ESTA LÍNEA AGREGA LA CONEXIÓN
            }
        )


        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color(0xFF36393F))
        ) {
            if (selectedChannel != null) {
                ChatRoomScreen(viewModel = viewModel, userId)
                //Text(
                    //"Canal: $selectedChannel",
                    //color = Color.White,
                    //modifier = Modifier.padding(16.dp)
                //)

            } else {
                Text(
                    "Selecciona un canal",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}