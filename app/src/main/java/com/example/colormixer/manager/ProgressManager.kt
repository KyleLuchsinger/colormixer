package com.example.colormixer.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// Class to manage level progression and completion state
class ProgressManager private constructor(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private var _currentLevel by mutableStateOf(1)
    val currentLevel: Int get() = _currentLevel

    private var _highestUnlockedLevel by mutableStateOf(1)
    val highestUnlockedLevel: Int get() = _highestUnlockedLevel

    init {
        // Load saved progress
        _highestUnlockedLevel = prefs.getInt(KEY_HIGHEST_UNLOCKED, 1)
        _currentLevel = prefs.getInt(KEY_CURRENT_LEVEL, 1)
    }

    fun isLevelUnlocked(levelNumber: Int): Boolean {
        return levelNumber <= _highestUnlockedLevel
    }

    fun isLevelCompleted(levelNumber: Int): Boolean {
        return levelNumber < _highestUnlockedLevel
    }

    fun completeLevel(levelNumber: Int) {
        if (levelNumber == _highestUnlockedLevel) {
            // Unlock next level
            _highestUnlockedLevel = minOf(_highestUnlockedLevel + 1, MAX_LEVELS)
            prefs.edit().putInt(KEY_HIGHEST_UNLOCKED, _highestUnlockedLevel).apply()
        }
    }

    fun updateCurrentLevel(level: Int) {
        if (level in 1..MAX_LEVELS && level <= _highestUnlockedLevel) {
            _currentLevel = level
            prefs.edit().putInt(KEY_CURRENT_LEVEL, level).apply()
        }
    }

    companion object {
        private const val PREFS_NAME = "level_progress"
        private const val KEY_HIGHEST_UNLOCKED = "highest_unlocked"
        private const val KEY_CURRENT_LEVEL = "current_level"
        private const val MAX_LEVELS = 10 // Update this when adding more levels

        @Volatile
        private var INSTANCE: ProgressManager? = null

        fun getInstance(context: Context): ProgressManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProgressManager(context).also { INSTANCE = it }
            }
        }
    }
}