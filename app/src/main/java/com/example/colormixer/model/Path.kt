package com.example.colormixer.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class Path(
    val startPoint: Offset,
    val color: Color,
    val points: MutableList<Offset> = mutableListOf()
)