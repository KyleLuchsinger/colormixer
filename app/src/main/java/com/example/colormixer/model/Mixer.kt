package com.example.colormixer.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

// Represents a color mixing point on the grid
data class Mixer(
    val position: Offset,
    val inputColors: MutableList<Color> = mutableListOf(),
    val maxInputs: Int = 2  // Default to binary mixer
)