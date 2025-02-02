package com.example.colormixer.ui.game

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import com.example.colormixer.manager.LevelManager
import com.example.colormixer.model.Level
import com.example.colormixer.model.Path
import com.example.colormixer.model.Receiver

class GameState(
    val level: Level?,
    initialPaths: List<Path> = emptyList(),
    initialReceiverStates: Map<Receiver, Boolean> = emptyMap()
) {
    var paths by mutableStateOf(initialPaths)
    var currentPath by mutableStateOf<Path?>(null)
    var lastPoint by mutableStateOf<Offset?>(null)
    var currentDragPosition by mutableStateOf<Offset?>(null)
    var showCompletionDialog by mutableStateOf(false)
    var receiverStates by mutableStateOf(initialReceiverStates)

    // Add function to reset state
    fun reset() {
        paths = emptyList()
        currentPath = null
        lastPoint = null
        currentDragPosition = null
        showCompletionDialog = false
        receiverStates = emptyMap()
    }
}

@Composable
fun rememberGameState(levelNumber: Int): GameState {
    val levelManager = remember { LevelManager() }
    val level = remember(levelNumber) { levelManager.getLevel(levelNumber) }
    return remember(levelNumber) { GameState(level) }
}