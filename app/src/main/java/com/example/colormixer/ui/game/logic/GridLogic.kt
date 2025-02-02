package com.example.colormixer.ui.game.logic

import androidx.compose.ui.geometry.Offset
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.abs
import kotlin.math.roundToInt

class GridLogic(private var boardSize: Float = 0f, private val gridSize: Int) {
    private var gridCenters: List<Offset> = emptyList()
    private var cellSize: Float = 0f

    init {
        if (boardSize > 0) {
            initializeGrid()
        }
    }

    fun setBoardSize(size: Float) {
        if (size == boardSize) return
        boardSize = size
        initializeGrid()
    }

    private fun initializeGrid() {
        cellSize = boardSize / gridSize
        gridCenters = buildList {
            for (y in 0 until gridSize) {
                for (x in 0 until gridSize) {
                    add(Offset(
                        x = x * cellSize + cellSize / 2,
                        y = y * cellSize + cellSize / 2
                    ))
                }
            }
        }
    }

    fun findNearestCenter(point: Offset): Offset {
        return gridCenters.minBy { center ->
            val dx = center.x - point.x
            val dy = center.y - point.y
            dx * dx + dy * dy  // Square of distance
        }
    }

    fun getGridCenters(): List<Offset> = gridCenters

    fun getGridIndex(gridPos: Offset): Int {
        val x = (gridPos.x).toInt()
        val y = (gridPos.y).toInt()
        return y * gridSize + x
    }

    fun isValidGridMove(from: Offset, to: Offset): Boolean {
        // Find the actual grid coordinates of the centers
        val fromCenter = gridCenters.firstOrNull { it == from } ?: return false
        val toCenter = gridCenters.firstOrNull { it == to } ?: return false

        // Calculate the difference in terms of cell units
        val dx = abs((toCenter.x - fromCenter.x) / cellSize)
        val dy = abs((toCenter.y - fromCenter.y) / cellSize)

        // Allow only orthogonal movement
        return (dx != 0f && dy == 0f) || (dx == 0f && dy != 0f)
    }

    // Helper function to get grid coordinates
    private fun getGridCoordinates(point: Offset): Pair<Int, Int> {
        val x = ((point.x - cellSize / 2) / cellSize).roundToInt()
        val y = ((point.y - cellSize / 2) / cellSize).roundToInt()
        return Pair(x.coerceIn(0, gridSize - 1), y.coerceIn(0, gridSize - 1))
    }
}