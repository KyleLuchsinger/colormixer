package com.example.colormixer.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import com.example.colormixer.model.*
import com.example.colormixer.model.Path
import com.example.colormixer.ui.game.logic.GameLogic
import com.example.colormixer.util.ColorMixer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GameCanvas(
    gameState: GameState,
    gameLogic: GameLogic,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .onSizeChanged { size ->
                gameLogic.setCanvasSize(size.width.toFloat())
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        gameLogic.handleDragStart(offset)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        gameLogic.handleDrag(change.position)
                    },
                    onDragEnd = {
                        gameLogic.handleDragEnd()
                    }
                )
            }
    ) {
        val gridSize = gameState.level?.gridSize ?: 6
        val cellSize = size.width / gridSize

        drawStars(gridSize, cellSize)
        gameState.paths.forEach { drawPath(it) }
        drawCurrentPath(gameState)

        // Draw debug grid centers
        val centers = gameLogic.getGridCenters()
        centers.forEach { center ->
            drawCircle(
                color = Color.Yellow,
                radius = 4f,  // Small dot
                center = center,
                alpha = 0.8f  // Slightly transparent
            )
        }

        drawSources(gameState.level?.sources ?: emptyList(), cellSize)
        drawMixers(gameState.level?.mixers ?: emptyList(), cellSize, gameState)
        drawReceivers(gameState.level?.receivers ?: emptyList(), gameState.receiverStates, cellSize)
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStars(
    gridSize: Int,
    cellSize: Float
) {
    repeat(gridSize) { row ->
        repeat(gridSize) { col ->
            val center = Offset(
                x = col * cellSize + cellSize / 2,
                y = row * cellSize + cellSize / 2
            )

            val starSize = cellSize * 0.13f
            val points = 4
            val innerRadius = starSize * 0.4f
            val outerRadius = starSize

            val path = Path().apply {
                for (i in 0 until points * 2) {
                    val radius = if (i % 2 == 0) outerRadius else innerRadius
                    val angle = PI * i / points - PI / 2
                    val x = center.x + cos(angle) * radius
                    val y = center.y + sin(angle) * radius
                    if (i == 0) moveTo(x.toFloat(), y.toFloat())
                    else lineTo(x.toFloat(), y.toFloat())
                }
                close()
            }

            drawPath(
                path = path,
                color = Color.White.copy(alpha = 0.2f),
                style = Fill
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPath(path: Path) {
    if (path.points.size > 1) {
        for (i in 0 until path.points.size - 1) {
            drawLine(
                color = path.color,
                start = path.points[i],
                end = path.points[i + 1],
                strokeWidth = 12f,
                cap = StrokeCap.Round
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCurrentPath(gameState: GameState) {
    gameState.currentPath?.let { path ->
        drawPath(path)
        gameState.lastPoint?.let { last ->
            gameState.currentDragPosition?.let { current ->
                drawLine(
                    color = path.color.copy(alpha = 0.5f),
                    start = last,
                    end = current,
                    strokeWidth = 12f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSources(
    sources: List<PowerSource>,
    cellSize: Float
) {
    sources.forEach { source ->
        val center = Offset(
            x = source.position.x * cellSize + cellSize / 2,
            y = source.position.y * cellSize + cellSize / 2
        )

        // Main source circle with white outline
        drawCircle(
            color = Color.White,
            radius = cellSize / 3.2f,
            center = center,
            style = Stroke(width = 3f)
        )

        // Color fill
        drawCircle(
            color = source.color,
            radius = cellSize / 3.5f,
            center = center
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawMixers(
    mixers: List<Mixer>,
    cellSize: Float,
    gameState: GameState
) {
    mixers.forEach { mixer ->
        val center = Offset(
            x = mixer.position.x * cellSize + cellSize / 2,
            y = mixer.position.y * cellSize + cellSize / 2
        )

        val inputPaths = gameState.paths.filter { path ->
            path.points.last() == center
        }

        val mixedColor = if (inputPaths.isNotEmpty()) {
            ColorMixer.mixColors(inputPaths.map { it.color })
        } else {
            null
        }

        // Draw outer circle
        drawCircle(
            color = Color.White,
            radius = cellSize / 3f,
            center = center,
            style = Stroke(width = 4f)
        )

        // Draw inner circle
        if (mixedColor != null) {
            drawCircle(
                color = mixedColor,
                radius = cellSize / 4f,
                center = center,
                alpha = 0.6f
            )
        } else {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.White.copy(alpha = 0.1f)
                    ),
                    center = center,
                    radius = cellSize / 4f
                ),
                radius = cellSize / 4f,
                center = center
            )
        }

        // Draw "+" symbol
        val symbolSize = cellSize / 6f
        drawLine(
            color = Color.White,
            start = Offset(center.x - symbolSize, center.y),
            end = Offset(center.x + symbolSize, center.y),
            strokeWidth = 10f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.White,
            start = Offset(center.x, center.y - symbolSize),
            end = Offset(center.x, center.y + symbolSize),
            strokeWidth = 10f,
            cap = StrokeCap.Round
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawReceivers(
    receivers: List<Receiver>,
    receiverStates: Map<Receiver, Boolean>,
    cellSize: Float
) {
    receivers.forEach { receiver ->
        val center = Offset(
            x = receiver.position.x * cellSize + cellSize / 2,
            y = receiver.position.y * cellSize + cellSize / 2
        )

        // Base receiver circle with white outline
        drawCircle(
            color = Color.White,
            radius = cellSize / 3f,
            center = center,
            style = Stroke(width = 4f)
        )

        // Target color fill
        drawCircle(
            color = receiver.targetColor,
            radius = cellSize / 3.5f,
            center = center
        )

        // Down arrow
        val arrowSize = cellSize * 0.2f
        val arrowTop = center.y - arrowSize * 0.5f
        val arrowBottom = center.y + arrowSize * 0.5f

        val isReceiverSatisfied = receiverStates[receiver] == true
        val arrowBrush = if (isReceiverSatisfied) {
            // Cosmic rainbow gradient for satisfied receivers
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFF1E88E5),  // Bright blue
                    Color(0xFF7E57C2),  // Purple
                    Color(0xFFE91E63),  // Pink
                    Color(0xFF3F51B5),  // Deep blue
                    Color(0xFF9C27B0)   // Rich purple
                ),
                start = Offset(center.x - arrowSize, arrowTop),
                end = Offset(center.x + arrowSize, arrowBottom),
                tileMode = TileMode.Mirror
            )
        } else {
            // Solid white for unsatisfied receivers
            SolidColor(Color.White)
        }

        // Draw vertical line of arrow
        drawLine(
            brush = arrowBrush,
            start = Offset(center.x, arrowTop),
            end = Offset(center.x, arrowBottom),
            strokeWidth = 10f,
            cap = StrokeCap.Round
        )

        // Draw left diagonal of arrow
        drawLine(
            brush = arrowBrush,
            start = Offset(center.x, arrowBottom),
            end = Offset(center.x - arrowSize * 0.4f, arrowBottom - arrowSize * 0.4f),
            strokeWidth = 10f,
            cap = StrokeCap.Round
        )

        // Draw right diagonal of arrow
        drawLine(
            brush = arrowBrush,
            start = Offset(center.x, arrowBottom),
            end = Offset(center.x + arrowSize * 0.4f, arrowBottom - arrowSize * 0.4f),
            strokeWidth = 10f,
            cap = StrokeCap.Round
        )
    }
}