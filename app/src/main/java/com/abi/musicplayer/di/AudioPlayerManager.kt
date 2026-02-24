package com.abi.musicplayer.di

import android.content.Context
import android.media.MediaPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.net.toUri

class AudioPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioEffectsManager: AudioEffectsManager
) {

    private var mediaPlayer = MediaPlayer()

    init {
        audioEffectsManager.setupEqualizer(mediaPlayer.audioSessionId)
    }

    fun play(resId: Int) {
        val uri = "android.resource://${context.packageName}/$resId".toUri()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(context, uri)
        mediaPlayer.prepare()
        mediaPlayer.start()

        mediaPlayer.setOnCompletionListener {
            play(resId = resId)
        }
    }

    fun stop() {
        mediaPlayer.release()
        audioEffectsManager.release()
    }

    fun resume() {
        mediaPlayer.start()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun seekTo(position: Float) {
        mediaPlayer.seekTo(position.toInt())
    }

    fun togglePlayPause() {
        if (isAudioPlaying) {
            pause()
        } else {
            resume()
        }
    }

    val isAudioPlaying: Boolean
        get() = mediaPlayer.isPlaying

    val getCurrentPosition : Float
        get() = mediaPlayer.currentPosition.toFloat()

    val totalDuration : Float
        get() = mediaPlayer.duration.toFloat()

    val isAudioFinished : Boolean
        get() = getCurrentPosition >= totalDuration

}