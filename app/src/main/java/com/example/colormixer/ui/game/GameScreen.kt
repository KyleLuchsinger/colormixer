package com.example.colormixer.ui.game

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.colormixer.audio.SoundManager
import com.example.colormixer.ui.game.logic.GameLogic

@Composable
fun GameScreen(
    gameState: GameState,
    gameLogic: GameLogic,
    onLevelComplete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val soundManager = remember { SoundManager.getInstance(context) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Level header and tutorial
        LevelHeader(gameState.level)

        // Game board canvas
        GameCanvas(
            gameState = gameState,
            gameLogic = gameLogic,
            modifier = Modifier.size(60.dp * (gameState.level?.gridSize ?: 6))
        )

        // Control buttons
        GameControls(gameState, gameLogic)

        // Level completion dialog
        if (gameState.showCompletionDialog) {
            LevelCompleteDialog(
                levelNumber = gameState.level?.number ?: 1,
                onNextLevel = {
                    soundManager.playSuccessSound()
                    gameState.showCompletionDialog = false
                    // Reset game state before transitioning
                    gameState.paths = emptyList()
                    gameState.receiverStates = emptyMap()
                    gameState.currentPath = null
                    gameState.lastPoint = null
                    gameState.currentDragPosition = null
                    // Call the completion callback
                    onLevelComplete(gameState.level?.number ?: 1)
                }
            )
        }
    }
}