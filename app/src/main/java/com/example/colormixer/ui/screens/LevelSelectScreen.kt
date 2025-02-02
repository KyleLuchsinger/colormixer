package com.example.colormixer.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colormixer.manager.ProgressManager
import com.example.colormixer.ui.components.RainbowText
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class LevelInfo(
    val number: Int,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false
)

@Composable
fun LevelSelectScreen(
    onLevelSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val progressManager = remember { ProgressManager.getInstance(context) }

    // Create level info list based on progress
    val levels = remember(progressManager.highestUnlockedLevel) {
        List(10) { index ->
            val levelNumber = index + 1
            LevelInfo(
                number = levelNumber,
                isUnlocked = progressManager.isLevelUnlocked(levelNumber),
                isCompleted = progressManager.isLevelCompleted(levelNumber)
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RainbowText(
            text = "SELECT LEVEL",
            fontSize = 40.sp,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(levels) { level ->
                LevelCard(
                    level = level,
                    onLevelSelected = {
                        if (level.isUnlocked) {
                            progressManager.updateCurrentLevel(level.number)
                            onLevelSelected(level.number)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun LevelCard(
    level: LevelInfo,
    onLevelSelected: (Int) -> Unit
) {
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

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(
                enabled = level.isUnlocked,
                onClick = { onLevelSelected(level.number) }
            ),
        colors = CardDefaults.cardColors(
            containerColor = when {
                !level.isUnlocked -> Color(0xFF1A1A2A)
                level.isCompleted -> Color(0xFF1A2A4A)
                else -> Color(0xFF1A1A3A)
            }
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Background glow for unlocked levels
            if (level.isUnlocked) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(glowAlpha)
                ) {
                    // Rotating glow effect
                    rotate(45f) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF1E88E5).copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            ),
                            radius = size.minDimension / 1.5f,
                            center = center
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Level number
                Text(
                    text = level.number.toString(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (level.isUnlocked) Color.White else Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (!level.isUnlocked) {
                    // Lock icon for locked levels
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                } else if (level.isCompleted) {
                    // Star for completed levels
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Completed",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}