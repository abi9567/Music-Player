package com.abi.musicplayer.ui.screens.musicListingScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abi.musicplayer.data.model.AudioFile
import com.abi.musicplayer.data.repository.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicListingViewModel @Inject constructor(
    audioRepository: AudioRepository
) : ViewModel() {

    private val _musicFiles = MutableStateFlow<List<AudioFile?>?>(value = emptyList())
    val musicFiles : StateFlow<List<AudioFile?>?> = _musicFiles.asStateFlow()

    init {
        viewModelScope.launch {
            _musicFiles.emit(audioRepository.getAudioFileDetails())
        }
    }

}