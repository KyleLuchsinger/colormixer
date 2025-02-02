package com.example.colormixer.manager

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.colormixer.model.Level
import com.example.colormixer.model.PowerSource
import com.example.colormixer.model.Receiver
import com.example.colormixer.model.Mixer

class LevelManager {
    private val levels = mutableListOf<Level>()

    init {
        initializeLevels()
    }

    private fun initializeLevels() {
        // Level 1: Simple Connection
        // Teach basic path drawing with a single color
        levels.add(Level(
            number = 1,
            gridSize = 5,
            sources = listOf(
                PowerSource(Offset(0f, 0f), Color.Red)
            ),
            receivers = listOf(
                Receiver(Offset(4f, 4f), Color.Red)
            ),
            tutorial = "Draw a path from the source to match the receiver's color!"
        ))

        // Level 2: Basic Mixing
        // Introduce color mixing with Red + Blue = Purple
        levels.add(Level(
            number = 2,
            gridSize = 5,
            sources = listOf(
                PowerSource(Offset(0f, 0f), Color.Red),
                PowerSource(Offset(4f, 0f), Color.Blue)
            ),
            receivers = listOf(
                Receiver(Offset(2f, 4f), Color(0xFF800080)) // Purple
            ),
            mixers = listOf(
                Mixer(Offset(2f, 2f))
            ),
            tutorial = "Mix Red and Blue in the mixer to create Purple!"
        ))

        // Level 3: Primary Colors
        // Practice basic routing with two receivers
        levels.add(Level(
            number = 3,
            gridSize = 5,
            sources = listOf(
                PowerSource(Offset(0f, 0f), Color.Red),
                PowerSource(Offset(4f, 0f), Color.Blue)
            ),
            receivers = listOf(
                Receiver(Offset(0f, 4f), Color.Red),
                Receiver(Offset(4f, 4f), Color.Blue)
            ),
            tutorial = "Route colors to their matching receivers. Remember, paths can't cross!"
        ))

        // Level 4: Basic Color Theory
        // Introduce Red + Green = Yellow
        levels.add(Level(
            number = 4,
            gridSize = 5,
            sources = listOf(
                PowerSource(Offset(0f, 0f), Color.Red),
                PowerSource(Offset(4f, 0f), Color.Green)
            ),
            receivers = listOf(
                Receiver(Offset(2f, 4f), Color(0xFFFFD700)), // Yellow
                Receiver(Offset(4f, 4f), Color.Green)
            ),
            mixers = listOf(
                Mixer(Offset(2f, 2f))
            ),
            tutorial = "Mix Red and Green to create Yellow, while also routing Green!"
        ))

        // Level 5: Multiple Mixers
        // Two separate mixing operations
        levels.add(Level(
            number = 5,
            gridSize = 6,
            sources = listOf(
                PowerSource(Offset(0f, 0f), Color.Blue),
                PowerSource(Offset(5f, 0f), Color.Red)
            ),
            receivers = listOf(
                Receiver(Offset(1f, 5f), Color(0xFF800080)), // Purple
                Receiver(Offset(4f, 5f), Color(0xFF4B0082))  // Darker Purple
            ),
            mixers = listOf(
                Mixer(Offset(1f, 2f)),
                Mixer(Offset(4f, 2f))
            ),
            tutorial = "Create different shades by mixing colors multiple times!"
        ))

        // Level 6: Complex Routing
        // Multiple sources and receivers requiring careful path planning
        levels.add(Level(
            number = 6,
            gridSize = 6,
            sources = listOf(
                PowerSource(Offset(0f, 0f), Color.Blue),
                PowerSource(Offset(5f, 0f), Color.Green),
                PowerSource(Offset(2f, 0f), Color.Red)
            ),
            receivers = listOf(
                Receiver(Offset(0f, 5f), Color.Blue),
                Receiver(Offset(2f, 5f), Color(0xFF800080)), // Purple
                Receiver(Offset(5f, 5f), Color.Green)
            ),
            mixers = listOf(
                Mixer(Offset(2f, 2f))
            ),
            tutorial = "Plan your paths carefully to avoid crossings!"
        ))

        // Level 7: Advanced Mixing
        // Multiple mixers with different combinations
        levels.add(Level(
            number = 7,
            gridSize = 6,
            sources = listOf(
                PowerSource(Offset(0f, 0f), Color.Red),
                PowerSource(Offset(5f, 0f), Color.Blue),
                PowerSource(Offset(2f, 0f), Color.Green)
            ),
            receivers = listOf(
                Receiver(Offset(0f, 5f), Color(0xFF800080)), // Purple
                Receiver(Offset(5f, 5f), Color(0xFF00FF00))  // Bright Green
            ),
            mixers = listOf(
                Mixer(Offset(1f, 2f)),
                Mixer(Offset(4f, 2f))
            ),
            tutorial = "Create multiple color combinations using different mixers!"
        ))

        // Level 8: Color Network
        // Complex routing with multiple mixers and receivers
        levels.add(Level(
            number = 8,
            gridSize = 6,
            sources = listOf(
                PowerSource(Offset(0f, 0f), Color.Blue),
                PowerSource(Offset(5f, 0f), Color.Red),
                PowerSource(Offset(2f, 0f), Color.Green)
            ),
            receivers = listOf(
                Receiver(Offset(0f, 5f), Color(0xFF800080)), // Purple
                Receiver(Offset(2f, 5f), Color(0xFF98FB98)), // Light Green
                Receiver(Offset(5f, 5f), Color(0xFF4B0082))  // Dark Purple
            ),
            mixers = listOf(
                Mixer(Offset(1f, 2f)),
                Mixer(Offset(3f, 2f)),
                Mixer(Offset(5f, 2f))
            ),
            tutorial = "Create a network of paths to mix multiple color variations!"
        ))

        // Level 9: Master Challenge I
        // Complex puzzle requiring careful planning
        levels.add(Level(
            number = 9,
            gridSize = 6,
            sources = listOf(
                PowerSource(Offset(0f, 0f), Color.Red),
                PowerSource(Offset(5f, 0f), Color.Blue),
                PowerSource(Offset(2f, 0f), Color.Green)
            ),
            receivers = listOf(
                Receiver(Offset(0f, 5f), Color(0xFF800080)), // Purple
                Receiver(Offset(2f, 5f), Color(0xFFFFD700)), // Yellow
                Receiver(Offset(4f, 5f), Color(0xFF98FB98)), // Light Green
                Receiver(Offset(5f, 5f), Color(0xFF4B0082))  // Dark Purple
            ),
            mixers = listOf(
                Mixer(Offset(1f, 2f)),
                Mixer(Offset(3f, 2f)),
                Mixer(Offset(5f, 2f)),
                Mixer(Offset(2f, 3f))
            ),
            tutorial = "Use everything you've learned to create a complex color network!"
        ))

        // Level 10: Grand Finale
        // Most complex level requiring mastery of all mechanics
        levels.add(Level(
            number = 10,
            gridSize = 6,
            sources = listOf(
                PowerSource(Offset(0f, 0f), Color.Red),
                PowerSource(Offset(5f, 0f), Color.Blue),
                PowerSource(Offset(2f, 0f), Color.Green)
            ),
            receivers = listOf(
                Receiver(Offset(0f, 5f), Color(0xFF800080)), // Purple
                Receiver(Offset(1f, 5f), Color(0xFFFFD700)), // Yellow
                Receiver(Offset(3f, 5f), Color(0xFF4B0082)), // Dark Purple
                Receiver(Offset(4f, 5f), Color(0xFF98FB98)), // Light Green
                Receiver(Offset(5f, 5f), Color(0xFF483D8B))  // Dark Slate Blue
            ),
            mixers = listOf(
                Mixer(Offset(1f, 2f)),
                Mixer(Offset(3f, 2f)),
                Mixer(Offset(5f, 2f)),
                Mixer(Offset(2f, 3f)),
                Mixer(Offset(4f, 3f))
            ),
            tutorial = "The ultimate challenge! Master color mixing and path planning!"
        ))
    }

    fun getLevel(levelNumber: Int): Level? {
        return levels.getOrNull(levelNumber - 1)
    }

    fun getTotalLevels(): Int = levels.size
}