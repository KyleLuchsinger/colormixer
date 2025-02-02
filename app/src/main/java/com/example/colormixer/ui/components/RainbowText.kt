package com.example.colormixer.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RainbowText(
    text: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        Color(0xFF1E88E5),  // Bright blue
        Color(0xFF7E57C2),  // Purple
        Color(0xFFE91E63),  // Pink
        Color(0xFF3F51B5),  // Deep blue
        Color(0xFF9C27B0),  // Rich purple
        Color(0xFF1E88E5)   // Back to bright blue for smooth transition
    )

    val transition = rememberInfiniteTransition(label = "Cosmic")
    val offset by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Gradient Offset"
    )

    // Calculate diagonal movement using angle (45 degrees = PI/4 radians)
    val angle = PI / 4  // 45 degree angle
    val xOffset = cos(angle) * offset * 1500f
    val yOffset = sin(angle) * offset * 1500f

    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(xOffset.toFloat(), yOffset.toFloat()),
        end = Offset((xOffset + 800f * cos(angle)).toFloat(), (yOffset + 800f * sin(angle)).toFloat()),
        tileMode = TileMode.Mirror
    )

    val style = TextStyle(
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        brush = brush
    )

    BasicText(
        text = text,
        modifier = modifier,
        style = style,
        maxLines = 1,
        softWrap = false
    )
}