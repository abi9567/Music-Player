package com.abi.musicplayer.ui.screens.musicPlayerScreen

import androidx.compose.material3.FabPosition
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abi.musicplayer.data.model.AudioFile
import com.abi.musicplayer.data.repository.AudioRepository
import com.abi.musicplayer.di.AudioPlayerManager
import com.abi.musicplayer.navigation.Screens.Companion.FILE_ID_ARGS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val audioRepository: AudioRepository,
    private val audioPlayerManager: AudioPlayerManager,
    state: SavedStateHandle
) : ViewModel() {

    private val musicId = state.get<String>(FILE_ID_ARGS)?.toInt()

    val musicFiles = audioRepository.audioFiles.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 1000),
        initialValue = emptyList()
    )

    private val _currentAudio = MutableStateFlow<AudioFile?>(value = null)
    val currentAudio: StateFlow<AudioFile?> = _currentAudio.asStateFlow()

    private val _sliderPosition = MutableStateFlow(value = 0F)
    val sliderPosition: StateFlow<Float> = _sliderPosition.asStateFlow()

    private val _isPlaying = MutableStateFlow(value = false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    val nextAudioFile = musicFiles.combine(_currentAudio) { list, currentAudio ->
        val currentIndex = list.indexOf(currentAudio).takeIf { it != -1 } ?: return@combine null
        list.getOrNull(index = currentIndex + 1)
    }

    val previousAudioFile = musicFiles.combine(_currentAudio) { list, currentAudio ->
        val currentIndex = list.indexOf(currentAudio).takeIf { it != -1 } ?: return@combine null
        list.getOrNull(index = currentIndex + 1)
    }

    private var slidingJob: Job? = null

    init {
        viewModelScope.launch {
            val music = audioRepository.fetchMusic(id = musicId)
            _currentAudio.emit(value = music)
            startPlaying(resId = music?.id)
        }
    }

    fun changeSliderPosition(position: Float) {
        viewModelScope.launch {
            slidingJob?.cancel()
            _sliderPosition.emit(value = position)
        }
    }

    fun seekPosition() {
        audioPlayerManager.seekTo(position = _sliderPosition.value)
        updateSliderPosition()
    }

    fun startPlaying(resId: Int?) {
        resId ?: return
        viewModelScope.launch {
            audioPlayerManager.play(resId = resId)
            _isPlaying.emit(value = true)
            updateSliderPosition()
        }
    }

    fun togglePlayPause() {
        viewModelScope.launch {
            audioPlayerManager.togglePlayPause()
            _isPlaying.emit(value = !_isPlaying.value)
        }
    }

    fun playNextAudio() {
        viewModelScope.launch {
            val nextAudio = nextAudioFile.firstOrNull() ?: return@launch
            audioPlayerManager.play(resId = nextAudio.id)
        }
    }

    fun playPreviousAudio() {
        viewModelScope.launch {
            val nextAudio = previousAudioFile.firstOrNull() ?: return@launch
            audioPlayerManager.play(resId = nextAudio.id)
        }
    }

    fun updateSliderPosition() {
        slidingJob = viewModelScope.launch {
            while(true) {
                _sliderPosition.emit(value = audioPlayerManager.getCurrentPosition)
                delay(500)
            }
        }
    }

    fun stopPlayer() {
        audioPlayerManager.stop()
    }

}