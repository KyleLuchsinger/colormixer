package com.example.colormixer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colormixer.audio.SoundManager
import com.example.colormixer.ui.components.CosmicSlider
import com.example.colormixer.ui.components.RainbowText

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val soundManager = remember { SoundManager.getInstance(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Title
        RainbowText(
            text = "SETTINGS",
            fontSize = 48.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Volume Controls
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.ui.graphics.Color(0xFF1A1A3A)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Music Volume
                CosmicSlider(
                    value = soundManager.musicVolume,
                    onValueChange = { soundManager.setMusicVolume(it) },
                    label = "Music Volume"
                )

                // Sound Effects Volume
                CosmicSlider(
                    value = soundManager.sfxVolume,
                    onValueChange = {
                        soundManager.setSfxVolume(it)
                        // Play a test sound when adjusting
                        soundManager.playConnectSound()
                    },
                    label = "Sound Effects"
                )
            }
        }
    }
}