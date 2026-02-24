package com.abi.musicplayer.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.abi.musicplayer.ui.screens.musicListingScreen.MusicListingScreen
import com.abi.musicplayer.ui.screens.musicListingScreen.MusicListingViewModel
import com.abi.musicplayer.ui.screens.musicPlayerScreen.MusicPlayerScreen
import com.abi.musicplayer.ui.screens.musicPlayerScreen.MusicPlayerViewModel

@Composable
fun NavigationGraph(navController: NavHostController, paddingValues: PaddingValues) {

    NavHost(navController = navController, startDestination = Screens.MusicListingScreen.route) {

        composable(route = Screens.MusicListingScreen.route) {
            val listingViewModel = hiltViewModel<MusicListingViewModel>()
            MusicListingScreen(
                viewModel = listingViewModel,
                navController = navController
            )
        }

        composable(route = Screens.MusicPlayerScreen.route) {
            val viewModel = hiltViewModel<MusicPlayerViewModel>()
            MusicPlayerScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}