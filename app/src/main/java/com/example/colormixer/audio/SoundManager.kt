package com.example.colormixer.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.colormixer.R

class SoundManager private constructor(context: Context) {
    private var soundPool: SoundPool
    private var mediaPlayer: MediaPlayer? = null

    // Sound effects
    private var soundConnectId: Int = 0
    private var soundIntersectId: Int = 0
    private var soundSuccessId: Int = 0
    private var soundErrorId: Int = 0

    // Volume states using private backing properties
    private var _sfxVolume by mutableStateOf(0.7f)
    val sfxVolume: Float get() = _sfxVolume

    private var _musicVolume by mutableStateOf(0.5f)
    val musicVolume: Float get() = _musicVolume

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(audioAttributes)
            .build()

        // Load sound effects
        soundConnectId = soundPool.load(context, R.raw.connect, 1)
        soundIntersectId = soundPool.load(context, R.raw.intersect, 1)
        soundSuccessId = soundPool.load(context, R.raw.success, 1)
        soundErrorId = soundPool.load(context, R.raw.error, 1)

        // Initialize background music
        initializeBackgroundMusic(context)
    }

    private fun initializeBackgroundMusic(context: Context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.background_music).apply {
            isLooping = true
            setVolume(_musicVolume, _musicVolume)
        }
    }

    fun setSfxVolume(volume: Float) {
        _sfxVolume = volume.coerceIn(0f, 1f)
    }

    fun setMusicVolume(volume: Float) {
        _musicVolume = volume.coerceIn(0f, 1f)
        mediaPlayer?.setVolume(_musicVolume, _musicVolume)
    }

    fun playConnectSound() {
        soundPool.play(soundConnectId, _sfxVolume, _sfxVolume, 1, 0, 1f)
    }

    fun playIntersectSound() {
        soundPool.play(soundIntersectId, _sfxVolume, _sfxVolume, 1, 0, 1f)
    }

    fun playSuccessSound() {
        soundPool.play(soundSuccessId, _sfxVolume, _sfxVolume, 1, 0, 1f)
    }

    fun playErrorSound() {
        soundPool.play(soundErrorId, _sfxVolume, _sfxVolume, 1, 0, 1f)
    }

    fun startBackgroundMusic() {
        mediaPlayer?.start()
    }

    fun pauseBackgroundMusic() {
        mediaPlayer?.pause()
    }

    fun release() {
        soundPool.release()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object {
        @Volatile
        private var INSTANCE: SoundManager? = null

        fun getInstance(context: Context): SoundManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SoundManager(context).also { INSTANCE = it }
            }
        }
    }
}