package com.abi.musicplayer.ui.screens.musicPlayerScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.abi.musicplayer.R
import com.abi.musicplayer.ui.common.MusicTopBar
import com.abi.musicplayer.utils.Utils.formatMillis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(
    viewModel: MusicPlayerViewModel,
    navController: NavController
) {
    val music by viewModel.currentAudio.collectAsStateWithLifecycle()
    val currentPosition by viewModel.sliderPosition.collectAsStateWithLifecycle(initialValue = 0F)
    val isPlaying = viewModel.isPlaying.collectAsStateWithLifecycle(initialValue = false)
    val nextAudioFile = viewModel.nextAudioFile.collectAsStateWithLifecycle(initialValue = null)
    val previousAudioFile = viewModel.previousAudioFile.collectAsStateWithLifecycle(initialValue = null)
    var isBottomSheetVisible by remember { mutableStateOf(value = false) }

    LifecycleResumeEffect(key1 = Unit) {
        if (!isPlaying.value) {
            viewModel.togglePlayPause()
        }
        onPauseOrDispose {
            if (isPlaying.value) {
                viewModel.togglePlayPause()
            }
        }
    }

    if (isBottomSheetVisible) {
        EqualizerScreen(
            viewModel = viewModel,
            onDismissRequest = { isBottomSheetVisible = false }
        )
    }

    Scaffold(
        topBar = {
            MusicTopBar(
                title = R.string.app_name,
                onNavigationButtonClick = {
                    viewModel.stopPlayer()
                    navController.navigateUp()
                }
            )
        },
        containerColor = colorResource(id = R.color.background_color)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .padding(all = dimensionResource(id = R.dimen.margin_large)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { isBottomSheetVisible = true }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    tint = colorResource(id = R.color.white),
                    contentDescription = null
                )
            }
            Spacer(
                modifier = Modifier.height(height = dimensionResource(id = R.dimen.margin_large))
            )
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(music?.thumbnail)
                        .crossfade(true)
                        .build(),
                    contentDescription = music?.fileName ?: "",
                    placeholder = painterResource(id = R.drawable.ic_thumbnail),
                    error = painterResource(id = R.drawable.ic_thumbnail),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ratio = 1F)
                        .clip(shape = RoundedCornerShape(size = dimensionResource(id = R.dimen.margin_normal))),
                )
                AudioGraphView(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(size = dimensionResource(id = R.dimen.margin_normal)))
                        .height(height = 100.dp)
                        .align(alignment = Alignment.BottomCenter)
                        .fillMaxWidth(),
                    isPlaying = isPlaying
                )
            }
            Spacer(
                modifier = Modifier.height(height = dimensionResource(id = R.dimen.margin_large))
            )
            Text(
                text = music?.fileName ?: "",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(id = R.color.white),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(
                modifier = Modifier.height(height = dimensionResource(id = R.dimen.margin_small))
            )
            Text(
                text = music?.artistName ?: "",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelSmall,
                color = colorResource(id = R.color.white),
            )
            Spacer(
                modifier = Modifier.height(height = dimensionResource(id = R.dimen.margin_large))
            )
            Slider(
                value = currentPosition,
                modifier = Modifier.fillMaxWidth(),
                valueRange = 0F..(music?.totalDuration?:0L).toFloat(),
                colors = SliderDefaults.colors(
                    activeTrackColor = colorResource(id = R.color.white),
                    thumbColor = colorResource(id = R.color.white)
                ), onValueChange = viewModel::changeSliderPosition,
                onValueChangeFinished = viewModel::seekPosition
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = currentPosition.toLong().formatMillis(),
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(id = R.color.white),
                )
                Text(
                    text = music?.totalDuration.formatMillis(),
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(id = R.color.white),
                )
            }
            Spacer(
                modifier = Modifier.height(height = dimensionResource(id = R.dimen.margin_large))
            )
            MusicControllers(
                isPlaying = isPlaying,
                onPreviousAudioClick = if (previousAudioFile.value != null) viewModel::playPreviousAudio else null,
                onNextAudioClick = if (nextAudioFile.value != null) viewModel::playNextAudio else null,
                onTogglePlayPause = viewModel::togglePlayPause
            )
        }
    }
}