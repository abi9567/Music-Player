package com.abi.musicplayer.data.model

import android.graphics.Bitmap

data class AudioFile(
    val id: Int,
    val fileName: String,
    val thumbnail: Bitmap?,
    val artistName: String,
    val totalDuration: Long
)

