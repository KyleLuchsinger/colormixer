package com.example.colormixer.ui.game.logic

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.colormixer.model.Mixer
import com.example.colormixer.model.Path
import com.example.colormixer.model.Receiver

class ValidationLogic {
    fun isValidMove(
        from: Offset,
        to: Offset,
        paths: List<Path>,
        currentPath: Path?,
        isMixerTile: (Offset) -> Boolean,
        isSourceTile: (Offset) -> Boolean,
        isGridMoveValid: (Offset, Offset) -> Boolean
    ): Boolean {
        if (!isGridMoveValid(from, to)) {
            return false
        }

        // Check if destination is a mixer
        if (!isMixerTile(to)) {
            val isDestinationOccupied = paths.any { path ->
                path.points.any { point -> point == to }
            }
            if (isDestinationOccupied) {
                return false
            }
        }

        // Check if destination is a source tile (unless it's our starting point)
        if (isSourceTile(to) && currentPath?.points?.size ?: 0 > 0) {
            return false
        }

        // Check for loops
        return currentPath?.points?.contains(to) != true
    }

    fun checkLevelCompletion(
        receivers: List<Receiver>,
        paths: List<Path>,
        getReceiverPaths: (Receiver) -> List<Path>
    ): Map<Receiver, Boolean> {
        return receivers.associateWith { receiver ->
            val incomingPaths = getReceiverPaths(receiver)
            if (incomingPaths.isEmpty()) false
            else colorsMatch(incomingPaths.first().color, receiver.targetColor)
        }
    }

    private fun colorsMatch(color1: Color, color2: Color, tolerance: Float = 0.2f): Boolean {
        return kotlin.math.abs(color1.red - color2.red) < tolerance &&
                kotlin.math.abs(color1.green - color2.green) < tolerance &&
                kotlin.math.abs(color1.blue - color2.blue) < tolerance
    }
}