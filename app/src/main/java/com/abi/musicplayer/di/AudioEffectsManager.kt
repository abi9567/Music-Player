package com.abi.musicplayer.di

import android.media.audiofx.Equalizer
import com.abi.musicplayer.data.model.AudioEffects
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioEffectsManager @Inject constructor() {

    private var audioEqualizer: Equalizer? = null

    fun setupEqualizer(audioSessionId: Int) {
        release()
        audioEqualizer = Equalizer(0, audioSessionId).apply {
            enabled = true
        }
    }

    fun setBandLevel(band: Int, level: Int) {
        audioEqualizer?.setBandLevel(band.toShort(), level.toShort())
    }

    fun usePreset(preset: Int) {
        audioEqualizer?.usePreset(preset.toShort())
    }

    fun release() {
        audioEqualizer?.release()
        audioEqualizer = null
    }

    fun setupAudioEffect(): AudioEffects? {
        val equalizer = audioEqualizer ?: return null
        val numberOfBands = equalizer.numberOfBands
        val bandLevelRange = equalizer.bandLevelRange

        val bandLevels = List(numberOfBands.toInt()) {
            equalizer.getBandLevel(it.toShort())
        }

        val presets = (0 until equalizer.numberOfPresets).map {
            equalizer.getPresetName(it.toShort())
        }

        return AudioEffects(
            minLevel = bandLevelRange[0].toInt(),
            maxLevel = bandLevelRange[1].toInt(),
            bandLevels = bandLevels,
            presets = presets
        )
    }
}