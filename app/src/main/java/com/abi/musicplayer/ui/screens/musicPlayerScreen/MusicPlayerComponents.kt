package com.abi.musicplayer.ui.screens.musicPlayerScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abi.musicplayer.R

@Composable
fun MusicControllers(
    isPlaying: State<Boolean>,
    onTogglePlayPause: () -> Unit,
    onNextAudioClick: (() -> Unit)? = null,
    onPreviousAudioClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        IconButton(
            onClick = { onPreviousAudioClick?.let { it() } },
            enabled = onPreviousAudioClick != null
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_previous),
                contentDescription = null,
                tint = if (onPreviousAudioClick != null) Color.Unspecified else colorResource(id = R.color.track_color)
            )
        }

        IconButton(onClick = onTogglePlayPause) {
            Icon(
                modifier = Modifier.size(size = 45.dp),
                painter = painterResource(
                    id = if (!isPlaying.value) R.drawable.ic_play else R.drawable.ic_pause
                ), contentDescription = null,
                tint = Color.Unspecified
            )
        }

        IconButton(
            onClick = { onNextAudioClick?.let { it() } },
            enabled = onNextAudioClick != null
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = null,
                tint = if (onNextAudioClick != null) Color.Unspecified else colorResource(id = R.color.track_color)
            )
        }
    }
}

@Preview
@Composable
private fun MusicControllersPreview() {
    MusicControllers(
        isPlaying = remember { mutableStateOf(value = false) },
        onTogglePlayPause = {},
        onNextAudioClick = {},
        onPreviousAudioClick = {}
    )
}