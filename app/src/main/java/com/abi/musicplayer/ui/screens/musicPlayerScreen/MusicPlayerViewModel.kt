package com.abi.musicplayer.ui.screens.musicPlayerScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abi.musicplayer.data.model.AudioEffects
import com.abi.musicplayer.data.model.AudioFile
import com.abi.musicplayer.data.repository.AudioRepository
import com.abi.musicplayer.di.AudioEffectsManager
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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val audioRepository: AudioRepository,
    private val audioPlayerManager: AudioPlayerManager,
    private val audioEffectsManager: AudioEffectsManager,
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

    private val _audioEffect = MutableStateFlow<AudioEffects?>(value = null)
    val audioEffect: StateFlow<AudioEffects?> = _audioEffect.asStateFlow()

    private val _selectedPreset = MutableStateFlow<String?>(value = null)
    val selectedPreset : StateFlow<String?> = _selectedPreset.asStateFlow()

    private var slidingJob: Job? = null

    init {
        viewModelScope.launch {
            val music = audioRepository.fetchMusic(id = musicId)
            _currentAudio.emit(value = music)
            startPlaying(resId = music?.id)
            setupEqualizer()
        }
    }

    fun changeSliderPosition(position: Float) {
        viewModelScope.launch {
            slidingJob?.cancel()
            _sliderPosition.emit(value = position)
        }
    }

    fun seekPosition(reset: Boolean = false) {
        audioPlayerManager.seekTo(position = if (reset) 0F else _sliderPosition.value)
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

    fun setupEqualizer() {
        val bands = audioEffectsManager.getBandCount()
        val range = audioEffectsManager.getBandLevelRange()
        val bandLevels = List(bands.toInt()) { audioEffectsManager.getBandLevel(it.toShort()) }
        val presets = audioEffectsManager.getAvailablePresets()

        viewModelScope.launch {
            _audioEffect.emit(
                AudioEffects(
                    minLevel = range[0].toInt(),
                    maxLevel = range[1].toInt(),
                    bandLevels = bandLevels,
                    presets = presets
                )
            )
        }
    }

    fun setBandLevel(band: Int, level: Float) {
        viewModelScope.launch {
            audioEffectsManager.setBandLevel(band.toShort(), level.toInt().toShort())
            val updatedBandLevel = _audioEffect.value?.copy(
                bandLevels = _audioEffect.value?.bandLevels?.toMutableList()?.apply {
                    this[band] = level.toInt().toShort()
                } ?: emptyList()
            )
            _audioEffect.emit(updatedBandLevel)
        }
    }

    fun setPreset(preset : Int, name : String) {
        viewModelScope.launch {
            audioEffectsManager.usePreset(preset = preset)
            _selectedPreset.emit(value = name)
        }
    }

    fun resetPreset() {
        viewModelScope.launch {
            audioEffectsManager.usePreset(preset = 0)
            _selectedPreset.emit(value = null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerManager.stop()
    }
}