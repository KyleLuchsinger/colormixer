package com.example.colormixer.model


data class Level(
    val number: Int,
    val gridSize: Int,
    val sources: List<PowerSource>,
    val receivers: List<Receiver>,
    val mixers: List<Mixer> = emptyList(),
    val tutorial: String? = null  // Optional tutorial text for the level
)