package com.example.colormixer.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class Receiver(
    val position: Offset,
    val targetColor: Color
)