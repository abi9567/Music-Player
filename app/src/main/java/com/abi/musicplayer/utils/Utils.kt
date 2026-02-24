package com.abi.musicplayer.utils

import kotlin.time.Duration.Companion.milliseconds

object Utils {
    fun Long?.formatMillis(): String {
        return (this?:0L).milliseconds.toComponents { minutes, seconds, _ ->
            "%02d:%02d".format(minutes, seconds)
        }
    }
}