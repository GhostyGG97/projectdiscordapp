package com.example.discordcloneapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.discordcloneapp.R

@Composable
fun StartupScreen(
    onClickRegister: () -> Unit,
    onClickLogin: () -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painterResource(R.drawable.logo),
                    null,
                    Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                Image(
                    painterResource(R.drawable.illustration),
                    null,
                    Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    "Welcome to Discord",
                    style = MaterialTheme.typography.h3,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "Join over 100 million people who use Discord to talk with communities and friends.",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

            }

            val staticElevation = ButtonDefaults.elevation(
                0.dp, 0.dp, 0.dp, 0.dp, 0.dp
            )

            Button(
                onClick = onClickRegister,
                Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                elevation = staticElevation
            ) {

                Text("Register")

            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onClickLogin,
                Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.surface,
                    contentColor = MaterialTheme.colors.onSurface
                ),
                elevation = staticElevation
            ) {

                Text("Login")

            }

        }

    }

}