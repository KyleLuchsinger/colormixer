package com.example.colormixer.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun CosmicSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    label: String
) {
    var animatedValue by remember { mutableStateOf(value) }

    val transition = rememberInfiniteTransition(label = "Cosmic")
    val glowAlpha by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Glow"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$label: ${(value * 100).roundToInt()}%",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Canvas(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val newX = (change.position.x / size.width).coerceIn(0f, 1f)
                        onValueChange(newX)
                        animatedValue = newX
                    }
                }
        ) {
            val width = size.width
            val height = size.height
            val sliderPosition = width * value

            // Draw background track
            drawLine(
                color = Color.White.copy(alpha = 0.2f),
                start = Offset(0f, height / 2),
                end = Offset(width, height / 2),
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Draw filled track with gradient
            val gradient = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF1E88E5),
                    Color(0xFF7E57C2)
                )
            )
            drawLine(
                brush = gradient,
                start = Offset(0f, height / 2),
                end = Offset(sliderPosition, height / 2),
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Draw handle
            drawCircle(
                color = Color.White,
                radius = 12.dp.toPx(),
                center = Offset(sliderPosition, height / 2)
            )

            // Draw glowing effect
            drawCircle(
                color = Color(0xFF7E57C2).copy(alpha = glowAlpha),
                radius = 16.dp.toPx(),
                center = Offset(sliderPosition, height / 2),
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}