package com.abi.musicplayer.ui.screens.musicPlayerScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.abi.musicplayer.R
import com.abi.musicplayer.ui.common.MusicTopBar

@Composable
fun MusicPlayerScreen(
    viewModel: MusicPlayerViewModel,
    navController: NavController
) {
    Scaffold(
        topBar = { MusicTopBar(title = R.string.app_name) },
        containerColor = colorResource(id = R.color.background_color)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(music.thumbnail)
                    .crossfade(true)
                    .build(),
                contentDescription = music.fileName,
                placeholder = painterResource(id = R.drawable.ic_thumbnail),
                error = painterResource(id = R.drawable.ic_thumbnail),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size = 56.dp)
                    .clip(RoundedCornerShape(size = dimensionResource(id = R.dimen.margin_normal))),
            )
        }
    }
}