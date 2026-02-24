package com.abi.musicplayer.di

import android.media.audiofx.Equalizer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioEffectsManager @Inject constructor() {

    private var equalizer: Equalizer? = null

    fun setupEqualizer(audioSessionId: Int) {
        release()
        equalizer = Equalizer(0, audioSessionId).apply {
            enabled = true
        }
    }

    fun getBandCount(): Short {
        return equalizer?.numberOfBands ?: 0
    }

    fun getBandLevelRange(): ShortArray {
        return equalizer?.bandLevelRange ?: shortArrayOf(0, 0)
    }

    fun getBandLevel(band: Short): Short {
        return equalizer?.getBandLevel(band) ?: 0
    }

    fun setBandLevel(band: Short, level: Short) {
        equalizer?.setBandLevel(band, level)
    }

    fun getAvailablePresets(): List<String> {
        val count = equalizer?.numberOfPresets?.toInt()?: 0
        return List(count) { i ->
            equalizer?.getPresetName(i.toShort()) ?: ""
        }
    }

    fun usePreset(preset: Int) {
        equalizer?.usePreset(preset.toShort())
    }

    fun release() {
        equalizer?.release()
        equalizer = null
    }
}