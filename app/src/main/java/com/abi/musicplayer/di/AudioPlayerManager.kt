package com.abi.musicplayer.di

import android.content.Context
import android.media.MediaPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AudioPlayerManager @Inject constructor(@ApplicationContext private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    fun play(resId: Int) {
        stop()
        mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer?.start()
    }

    fun stop() {
        mediaPlayer?.release()
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
        get() = ((mediaPlayer?.currentPosition?:0)).toFloat()

}