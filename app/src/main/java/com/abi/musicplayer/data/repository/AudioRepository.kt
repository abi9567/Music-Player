package com.abi.musicplayer.data.repository

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.abi.musicplayer.R
import com.abi.musicplayer.data.model.AudioFile
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.net.toUri

class AudioRepository @Inject constructor(@ApplicationContext private val context: Context) {
    fun getAudioFileDetails() : List<AudioFile?>? {
        val audioFiles = listOf(R.raw.mazha_thullikal_vettam)
        return audioFiles.mapIndexed { position, id ->
            try {
                val metadataRetriever = MediaMetadataRetriever()
                val uri = "android.resource://${context.packageName}/$id".toUri()
                metadataRetriever.setDataSource(context, uri)

                val fileName = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: "Music ${position + 1}"
                val artist = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "Unknow Artists"
                val mediaDuration = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L
                val mediaThumbnail = metadataRetriever.embeddedPicture?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
                metadataRetriever.release()
                AudioFile(
                    id = position,
                    fileName = fileName,
                    thumbnail = mediaThumbnail,
                    artistName = artist,
                    totalDuration = mediaDuration
                )
            } catch (error: Exception) {
                null
            }
        }
    }
}