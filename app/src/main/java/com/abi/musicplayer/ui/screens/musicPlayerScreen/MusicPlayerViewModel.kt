package com.abi.musicplayer.ui.screens.musicPlayerScreen

import android.media.AudioTrack
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class MusicPlayerViewModel : ViewModel() {

    val currentAudio by mutableStateOf<AudioTrack?>(null)
        private set

}