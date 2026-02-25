package com.abi.musicplayer.data.repository

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.abi.musicplayer.R
import com.abi.musicplayer.data.model.AudioFile
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.net.toUri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Singleton

@Singleton
class AudioRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val _audioFiles = MutableStateFlow<List<AudioFile?>>(emptyList())
    val audioFiles: StateFlow<List<AudioFile?>> = _audioFiles.asStateFlow()

    suspend fun getAudioFileDetails() {
        if (_audioFiles.value.isNotEmpty()) return
        val audioFilesRaw = listOf(R.raw.mazha_thullikal_vettam, R.raw.vaathil_melle_thuranna, R.raw.dhurandhar, R.raw.vettam_new)
        val audioFiles = audioFilesRaw.mapIndexed { position, id ->
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
                    id = id,
                    fileName = fileName,
                    thumbnail = mediaThumbnail,
                    artistName = artist,
                    totalDuration = mediaDuration
                )
            } catch (error: Exception) {
                null
            }
        }
        _audioFiles.emit(value = audioFiles)
    }

    fun fetchMusic(id: Int?): AudioFile?  {
        return _audioFiles.value.find { it?.id == id }
    }

}