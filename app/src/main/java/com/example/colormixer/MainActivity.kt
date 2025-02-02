package com.example.colormixer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.colormixer.audio.SoundManager
import com.example.colormixer.manager.ProgressManager
import com.example.colormixer.ui.theme.ColorMixerTheme
import com.example.colormixer.ui.screens.MainScreen
import com.example.colormixer.ui.screens.SettingsScreen
import com.example.colormixer.ui.game.GameBoard
import com.example.colormixer.ui.components.CosmicBackground
import com.example.colormixer.ui.screens.LevelSelectScreen

class MainActivity : ComponentActivity() {
    private lateinit var soundManager: SoundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize sound manager
        soundManager = SoundManager.getInstance(this)
        soundManager.startBackgroundMusic()

        setContent {
            ColorMixerTheme {
                ColorMixerApp()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        soundManager.pauseBackgroundMusic()
    }

    override fun onResume() {
        super.onResume()
        soundManager.startBackgroundMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundManager.release()
    }
}

@Composable
fun ColorMixerApp() {
    var currentScreen by remember { mutableStateOf(Screen.MainMenu) }
    var lastGameScreen by remember { mutableStateOf(Screen.MainMenu) }

    val context = LocalContext.current
    val progressManager = remember { ProgressManager.getInstance(context) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF000020)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background
            CosmicBackground()

            // Content with Navigation
            Box(modifier = Modifier.fillMaxSize()) {
                // Screen content
                Box(modifier = Modifier.fillMaxSize()) {
                    when (currentScreen) {
                        Screen.MainMenu -> MainScreen(
                            onPlayClicked = {
                                currentScreen = Screen.Game
                            },
                            onLevelSelectClicked = {
                                currentScreen = Screen.LevelSelect
                            }
                        )
                        Screen.Game -> GameBoard(
                            levelNumber = progressManager.currentLevel,
                            onLevelComplete = { completedLevel ->
                                // Mark the current level as completed
                                progressManager.completeLevel(completedLevel)

                                // Get the next level number
                                val nextLevel = completedLevel + 1

                                // If next level is unlocked, update current level and stay on game screen
                                if (nextLevel != 11) {
                                    progressManager.updateCurrentLevel(nextLevel)
                                    // Force GameBoard to recompose with new level
                                    currentScreen = Screen.MainMenu
                                    currentScreen = Screen.Game
                                } else {
                                    // If no more levels available, go to level select
                                    currentScreen = Screen.LevelSelect
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                        Screen.LevelSelect -> LevelSelectScreen(
                            onLevelSelected = { levelNumber ->
                                progressManager.updateCurrentLevel(levelNumber)
                                currentScreen = Screen.Game
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                        Screen.Settings -> SettingsScreen()
                    }
                }

                // Navigation overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Home button (only shown when not on main menu)
                    if (currentScreen != Screen.MainMenu) {
                        IconButton(
                            onClick = {
                                if (currentScreen == Screen.Settings) {
                                    currentScreen = lastGameScreen
                                } else {
                                    currentScreen = Screen.MainMenu
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    // Settings button (hidden in settings screen)
                    if (currentScreen != Screen.Settings) {
                        IconButton(
                            onClick = {
                                lastGameScreen = currentScreen
                                currentScreen = Screen.Settings
                            },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class Screen {
    MainMenu,
    Game,
    LevelSelect,
    Settings
}