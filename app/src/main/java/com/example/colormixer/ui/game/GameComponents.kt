package com.example.colormixer.ui.game

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colormixer.audio.SoundManager
import com.example.colormixer.model.Level
import com.example.colormixer.ui.components.RainbowText
import com.example.colormixer.ui.game.logic.GameLogic

@Composable
fun LevelHeader(level: Level?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Level number with rainbow effect
        RainbowText(
            text = "LEVEL ${level?.number ?: 1}",
            fontSize = 40.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Tutorial text if available
        level?.tutorial?.let { tutorial ->
            Text(
                text = tutorial,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}

@Composable
fun GameControls(gameState: GameState, gameLogic: GameLogic) {
    val context = LocalContext.current
    val soundManager = remember { SoundManager.getInstance(context) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        // Undo button
        Button(
            onClick = {
                if (gameState.paths.isNotEmpty()) {
                    gameState.paths = gameState.paths.dropLast(1)
                    soundManager.playConnectSound()
                    // Recheck receiver states after undoing
                    gameState.level?.let { level ->
                        gameState.receiverStates = level.receivers.associateWith { receiver ->
                            val incomingPaths = gameState.paths.filter { path ->
                                path.points.last() == gameLogic.getGridCenters()[gameLogic.getGridIndex(receiver.position)]
                            }
                            if (incomingPaths.isEmpty()) false
                            else {
                                val color1 = incomingPaths.first().color
                                val color2 = receiver.targetColor
                                val tolerance = 0.2f
                                kotlin.math.abs(color1.red - color2.red) < tolerance &&
                                        kotlin.math.abs(color1.green - color2.green) < tolerance &&
                                        kotlin.math.abs(color1.blue - color2.blue) < tolerance
                            }
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3F51B5),
                contentColor = Color.White
            ),
            modifier = Modifier.width(120.dp)
        ) {
            Text(
                text = "Undo",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Clear button
        Button(
            onClick = {
                gameState.paths = emptyList()
                soundManager.playConnectSound()
                // Reset receiver states when clearing
                gameState.receiverStates = emptyMap()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3F51B5),
                contentColor = Color.White
            ),
            modifier = Modifier.width(120.dp)
        ) {
            Text(
                text = "Clear",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LevelCompleteDialog(
    levelNumber: Int,
    onNextLevel: () -> Unit
) {
    val context = LocalContext.current
    val soundManager = remember { SoundManager.getInstance(context) }

    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(
                text = "Level Complete!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Congratulations! You've completed level $levelNumber!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    soundManager.playSuccessSound()
                    onNextLevel()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Next Level",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        },
        containerColor = Color(0xFF1A1A3A),
        modifier = Modifier
            .padding(16.dp)
            .widthIn(max = 400.dp)
    )
}

@Composable
fun ColorMixingHint(
    color1: Color,
    color2: Color,
    resultColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // First color circle
        Surface(
            color = color1,
            modifier = Modifier.size(24.dp),
            shape = CircleShape
        ) { }

        Text(
            text = "+",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        // Second color circle
        Surface(
            color = color2,
            modifier = Modifier.size(24.dp),
            shape = CircleShape
        ) { }

        Text(
            text = "=",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        // Result color circle
        Surface(
            color = resultColor,
            modifier = Modifier.size(24.dp),
            shape = CircleShape
        ) { }
    }
}

@Composable
fun LevelProgressIndicator(
    currentLevel: Int,
    totalLevels: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress = currentLevel.toFloat() / totalLevels,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = Color(0xFF4CAF50),
            trackColor = Color.White.copy(alpha = 0.2f)
        )

        Text(
            text = "$currentLevel / $totalLevels",
            color = Color.White.copy(alpha = 0.7f),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}