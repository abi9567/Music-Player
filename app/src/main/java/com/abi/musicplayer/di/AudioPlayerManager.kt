package com.abi.musicplayer.di

import android.content.Context
import android.media.MediaPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.net.toUri
import com.abi.musicplayer.data.model.AudioEffects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AudioPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioEffectsManager: AudioEffectsManager
) {

    private var mediaPlayer: MediaPlayer? = null

    private val _equalizerState = MutableStateFlow<AudioEffects?>(null)
    val equalizerState: StateFlow<AudioEffects?> = _equalizerState.asStateFlow()

    fun play(resId: Int) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }

        val uri = "android.resource://${context.packageName}/$resId".toUri()
        mediaPlayer?.apply {
            reset()
            setDataSource(context, uri)
            setOnPreparedListener {
                audioEffectsManager.setupEqualizer(audioSessionId)
                _equalizerState.value = audioEffectsManager.setupAudioEffect()
                isLooping = true
                start()
            }
            prepareAsync()
        }
    }

    fun setBandLevel(band: Int, level: Int) {
        audioEffectsManager.setBandLevel(band, level)
        _equalizerState.value = audioEffectsManager.setupAudioEffect()
    }

    fun setPreset(preset: Int) {
        audioEffectsManager.usePreset(preset)
        _equalizerState.value = audioEffectsManager.setupAudioEffect()
    }

    fun stop() {
        mediaPlayer?.release()
        audioEffectsManager.release()
        mediaPlayer = null
    }

    fun resume() {
        mediaPlayer?.start()
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun seekTo(position: Float) {
        mediaPlayer?.seekTo(position.toInt())
    }

    fun togglePlayPause() {
        if (isAudioPlaying) {
            pause()
        } else {
            resume()
        }
    }

    val isAudioPlaying: Boolean
        get() = mediaPlayer?.isPlaying == true

    val getCurrentPosition : Float
        get() = (mediaPlayer?.currentPosition ?: 0).toFloat()
}