package com.example.colormixer.ui.game.logic

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.colormixer.model.*
import com.example.colormixer.ui.game.GameState
import com.example.colormixer.util.ColorMixer
import kotlin.math.roundToInt

class GameLogic(private val gameState: GameState) {
    private val gridLogic = GridLogic(0f, gameState.level?.gridSize ?: 6)
    private val intersectionLogic = IntersectionLogic()
    private val validationLogic = ValidationLogic()

    fun setCanvasSize(size: Float) {
        println("Setting canvas size: $size")
        gridLogic.setBoardSize(size)
    }

    fun getGridCenters() = gridLogic.getGridCenters()

    fun getGridIndex(gridPos: Offset) = gridLogic.getGridIndex(gridPos)

    fun handleDragStart(pointerPos: Offset) {
        println("Drag start at: $pointerPos")
        val nearestCenter = gridLogic.findNearestCenter(pointerPos)
        println("Nearest center: $nearestCenter")

        val source = gameState.level?.sources?.find { source ->
            val sourceCenter = gridLogic.getGridCenters()[gridLogic.getGridIndex(source.position)]
            sourceCenter == nearestCenter
        }

        val mixer = gameState.level?.mixers?.find { mixer ->
            val mixerCenter = gridLogic.getGridCenters()[gridLogic.getGridIndex(mixer.position)]
            mixerCenter == nearestCenter && getMixerInputs(mixer).isNotEmpty()
        }

        when {
            source != null -> {
                val sourceCenter = gridLogic.getGridCenters()[gridLogic.getGridIndex(source.position)]
                gameState.currentPath = Path(
                    startPoint = sourceCenter,
                    color = source.color,
                    points = mutableListOf(sourceCenter)
                )
                gameState.lastPoint = sourceCenter
                println("Started path from source at: $sourceCenter")
            }
            mixer != null -> {
                val mixerCenter = gridLogic.getGridCenters()[gridLogic.getGridIndex(mixer.position)]
                val mixedColor = calculateMixedColor(mixer)
                gameState.currentPath = Path(
                    startPoint = mixerCenter,
                    color = mixedColor,
                    points = mutableListOf(mixerCenter)
                )
                gameState.lastPoint = mixerCenter
                println("Started path from mixer at: $mixerCenter")
            }
        }
    }

    fun handleDrag(pointerPos: Offset) {
        val currentPath = gameState.currentPath ?: return
        val lastPoint = gameState.lastPoint ?: return

        println("Drag at: $pointerPos")
        val nearestCenter = gridLogic.findNearestCenter(pointerPos)
        println("Nearest center: $nearestCenter")

        // Always update the preview line position
        gameState.currentDragPosition = pointerPos

        // Skip if we haven't moved to a new center
        if (nearestCenter == lastPoint) {
            println("Same center as last point, skipping")
            return
        }

        // Validate the move
        if (!validationLogic.isValidMove(
                from = lastPoint,
                to = nearestCenter,
                paths = gameState.paths,
                currentPath = gameState.currentPath,
                isMixerTile = { pos -> isMixerTile(pos) },
                isSourceTile = { pos -> isSourceTile(pos) },
                isGridMoveValid = { from, to -> gridLogic.isValidGridMove(from, to) }
            )) {
            println("Invalid move detected")
            return
        }

        // Add point to path
        currentPath.points.add(nearestCenter)
        gameState.lastPoint = nearestCenter
        println("Added point to path: $nearestCenter")
    }

    fun handleDragEnd() {
        val currentPath = gameState.currentPath ?: return
        if (currentPath.points.size > 1) {
            val lastPoint = currentPath.points.last()
            if (isDestination(lastPoint) &&
                !intersectionLogic.hasIntersections(
                    currentPath,
                    gameState.paths,
                    { pos -> isDestination(pos) }
                )
            ) {
                gameState.paths = gameState.paths + currentPath
                checkLevelCompletion()
            }
        }

        // Reset drag state
        gameState.currentPath = null
        gameState.lastPoint = null
        gameState.currentDragPosition = null
    }

    private fun isDestination(point: Offset): Boolean {
        return gameState.level?.let { level ->
            level.receivers.any { receiver ->
                gridLogic.getGridCenters()[gridLogic.getGridIndex(receiver.position)] == point
            } || level.mixers.any { mixer ->
                gridLogic.getGridCenters()[gridLogic.getGridIndex(mixer.position)] == point
            }
        } ?: false
    }

    private fun isMixerTile(point: Offset): Boolean {
        return gameState.level?.mixers?.any { mixer ->
            gridLogic.getGridCenters()[gridLogic.getGridIndex(mixer.position)] == point
        } ?: false
    }

    private fun isSourceTile(point: Offset): Boolean {
        return gameState.level?.sources?.any { source ->
            gridLogic.getGridCenters()[gridLogic.getGridIndex(source.position)] == point
        } ?: false
    }

    private fun getMixerInputs(mixer: Mixer): List<Path> {
        val mixerCenter = gridLogic.getGridCenters()[gridLogic.getGridIndex(mixer.position)]
        return gameState.paths.filter { path ->
            path.points.last() == mixerCenter
        }
    }

    private fun calculateMixedColor(mixer: Mixer): Color {
        val inputs = getMixerInputs(mixer)
        val mixedColor = ColorMixer.mixColors(inputs.map { it.color })

        // Convert colors to hex and log them
        val inputHexColors = inputs.map { path ->
            val c = path.color
            String.format("#%02X%02X%02X",
                (c.red * 255).toInt(),
                (c.green * 255).toInt(),
                (c.blue * 255).toInt()
            )
        }
        val resultHex = String.format("#%02X%02X%02X",
            (mixedColor.red * 255).toInt(),
            (mixedColor.green * 255).toInt(),
            (mixedColor.blue * 255).toInt()
        )

        println("Mixer at (${mixer.position.x}, ${mixer.position.y}):")
        println("Input colors: ${inputHexColors.joinToString(", ")}")
        println("Mixed result: $resultHex")
        println("---")

        return mixedColor
    }

    private fun completePath() {
        gameState.currentPath?.let { path ->
            if (path.points.size > 1 && !intersectionLogic.hasIntersections(
                    path,
                    gameState.paths,
                    { pos -> isDestination(pos) }
                )) {
                gameState.paths = gameState.paths + path
                checkLevelCompletion()
            }
        }
        gameState.currentPath = null
        gameState.lastPoint = null
        gameState.currentDragPosition = null
    }

    private fun checkLevelCompletion() {
        gameState.level?.let { level ->
            gameState.receiverStates = validationLogic.checkLevelCompletion(
                level.receivers,
                gameState.paths
            ) { receiver ->
                findIncomingPaths(receiver)
            }

            if (gameState.receiverStates.values.all { it }) {
                gameState.showCompletionDialog = true
            }
        }
    }

    private fun findIncomingPaths(receiver: Receiver): List<Path> {
        val receiverCenter = gridLogic.getGridCenters()[gridLogic.getGridIndex(receiver.position)]
        return gameState.paths.filter { path ->
            path.points.last() == receiverCenter
        }
    }
}