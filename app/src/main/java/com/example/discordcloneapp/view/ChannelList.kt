package com.example.discordcloneapp.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.discordcloneapp.models.Channel
import com.example.discordcloneapp.viewmodel.ChatViewModel

@Composable
fun ChannelList(viewModel: ChatViewModel, onSelect: (Channel) -> Unit) {
    val channels = viewModel.channels

    Column {
        channels.forEach {
            Text(
                text = it.name,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(it) }
                    .padding(8.dp)
            )
        }

        var newChannel by remember { mutableStateOf("") }

        OutlinedTextField(
            value = newChannel,
            onValueChange = { newChannel = it },
            label = { Text("Nuevo canal") },
            trailingIcon = {
                IconButton(onClick = {
                    if (newChannel.isNotBlank()) {
                        viewModel.addChannel(newChannel, "user-id")
                        newChannel = ""
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                backgroundColor = Color(0xFF40444B)
            )
        )
    }
}