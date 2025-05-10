package com.example.discordcloneapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.discordcloneapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashEnd: (Boolean) -> Unit
) {

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        LaunchedEffect(Unit) {
            delay(500)
            onSplashEnd(false)
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painterResource(R.drawable.discord),
                null,
                Modifier.size(120.dp),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(Color.White)
            )

        }

    }

}