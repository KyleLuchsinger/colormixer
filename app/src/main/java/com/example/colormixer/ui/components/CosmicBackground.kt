package com.example.colormixer.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.delay
import kotlin.math.*
import kotlin.random.Random

@Composable
fun CosmicBackground() {
    var animationOffset by remember { mutableStateOf(0f) }

    // Star positions
    val stars = remember {
        List(200) {
            Triple(
                Random.nextFloat(),  // x position (0-1)
                Random.nextFloat(),  // y position (0-1)
                Random.nextFloat() * 3f + 1f  // size (1-4)
            )
        }
    }

    // Background animation - increased speed by reducing delay
    LaunchedEffect(Unit) {
        while(true) {
            delay(30)  // Reduced from 50 to 30 for faster animation
            animationOffset += 0.8f  // Increased from 0.5f to 0.8f for faster movement
            if (animationOffset > 1000f) animationOffset = 0f
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Draw nebula-like effects
        val gradientColors = listOf(
            Color(0xFF000040),
            Color(0xFF200040),
            Color(0xFF400060)
        )

        // Draw flowing nebula effects
        repeat(5) { i ->
            rotate(animationOffset + i * 72f) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = gradientColors,
                        center = Offset(
                            x = (size.width / 2) + cos(animationOffset * 0.01f + i) * (size.width * 0.3f),
                            y = (size.height / 2) + sin(animationOffset * 0.01f + i) * (size.height * 0.3f)
                        ),
                        radius = size.width * 0.8f
                    ),
                    radius = size.width * 0.5f,
                    alpha = 0.2f
                )
            }
        }

        // Draw twinkling stars with faster animation
        stars.forEach { (xRatio, yRatio, starSize) ->
            val x = xRatio * size.width
            val y = yRatio * size.height
            val twinkle = (sin(animationOffset * 0.10f + x * y) + 1) / 2
            drawCircle(
                color = Color.White,
                radius = starSize * twinkle,
                center = Offset(x, y),
                alpha = 0.6f * twinkle
            )
        }
    }
}