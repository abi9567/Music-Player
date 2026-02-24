package com.abi.musicplayer.data.model


data class AudioEffects(
    val minLevel: Int,
    val maxLevel: Int,
    val bandLevels: List<Short> = emptyList(),
    val presets: List<String> = emptyList()
)

