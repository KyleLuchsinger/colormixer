package com.example.colormixer.ui.game

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.colormixer.manager.LevelManager
import com.example.colormixer.model.Level

@Composable
fun GameBoard(
    levelNumber: Int,
    onLevelComplete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Create a new LevelManager instance for each recomposition
    val levelManager = remember { LevelManager() }

    // Create a key that changes when the level number changes
    val levelKey = remember(levelNumber) { levelNumber }

    // Use the key to force recreation of all game state when level changes
    key(levelKey) {
        val level = remember(levelNumber) { levelManager.getLevel(levelNumber) }
        val gameState = remember(levelNumber) {
            GameState(
                level = level,
                initialPaths = emptyList(),
                initialReceiverStates = emptyMap()
            )
        }
        val gameLogic = remember(levelNumber) { GameLogic(gameState) }

        GameScreen(
            gameState = gameState,
            gameLogic = gameLogic,
            onLevelComplete = onLevelComplete,
            modifier = modifier
        )
    }
}