package com.abi.musicplayer.ui.screens.musicListingScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.abi.musicplayer.R
import com.abi.musicplayer.navigation.Screens
import com.abi.musicplayer.ui.common.MusicTopBar

@Composable
fun MusicListingScreen(
    viewModel: MusicListingViewModel,
    navController: NavController
) {
    val musicFiles by viewModel.musicFiles.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { MusicTopBar(title = R.string.app_name) },
        containerColor = colorResource(id = R.color.background_color)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues = paddingValues),
            contentPadding = PaddingValues(all = dimensionResource(id = R.dimen.margin_large)),
            verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.margin_normal))
        ) {
            items(items = musicFiles) {music ->
                music ?: return@items
                ListingSingleItem(
                    music = music,
                    onClick = {
                        navController.navigate(route = Screens.MusicPlayerScreen.musicPlayerScreenArgs(id = music.id))
                    }
                )
            }
        }
    }
}