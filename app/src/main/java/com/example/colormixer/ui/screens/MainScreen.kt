package com.example.colormixer.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colormixer.audio.SoundManager
import com.example.colormixer.manager.ProgressManager
import com.example.colormixer.ui.components.RainbowText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onPlayClicked: () -> Unit,
    onLevelSelectClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val progressManager = remember { ProgressManager.getInstance(context) }
    val soundManager = remember { SoundManager.getInstance(context) }

    // Animation states for button scaling
    var playButtonScale by remember { mutableStateOf(1f) }
    var levelSelectButtonScale by remember { mutableStateOf(1f) }

    // Button style
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF3F51B5),
        contentColor = Color.White
    )

    // Function to handle button clicks with sound
    fun handleButtonClick(action: () -> Unit) {
        soundManager.playConnectSound()
        action()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title with rainbow effect
        RainbowText(
            text = "COLOR",
            fontSize = 96.sp,
        )

        RainbowText(
            text = "FLOW",
            fontSize = 96.sp,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // Menu Buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    handleButtonClick {
                        // Continue from the current level
                        progressManager.updateCurrentLevel(progressManager.currentLevel)
                        onPlayClicked()
                    }
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(56.dp),
                colors = buttonColors,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp,
                    hoveredElevation = 8.dp
                )
            ) {
                Text(
                    text = if (progressManager.currentLevel == 1) "Start Game" else "Continue Level ${progressManager.currentLevel}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = { handleButtonClick(onLevelSelectClicked) },
                modifier = Modifier
                    .width(200.dp)
                    .height(56.dp),
                colors = buttonColors,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp,
                    hoveredElevation = 8.dp
                )
            ) {
                Text(
                    "Level Select",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Display highest level reached
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Highest Level: ${progressManager.highestUnlockedLevel}",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}