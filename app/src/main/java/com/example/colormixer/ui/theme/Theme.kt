package com.example.colormixer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ColorMixerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}