package com.abi.musicplayer.navigation

sealed class Screens(val route: String) {
    data object MusicListingScreen : Screens(route = "music_listing_screen")
    data object MusicPlayerScreen : Screens(route = "music_player_screen/{$FILE_ID_ARGS}") {
        fun musicPlayerScreenArgs(id: Int) = "music_player_screen/$id"
    }

    companion object {
        const val FILE_ID_ARGS = "file_name_args"
    }
}