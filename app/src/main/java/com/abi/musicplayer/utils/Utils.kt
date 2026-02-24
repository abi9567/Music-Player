package com.abi.musicplayer.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import kotlin.time.Duration.Companion.milliseconds

object Utils {

    fun Long?.formatMillis(): String {
        return (this?:0L).milliseconds.toComponents { minutes, seconds, _ ->
            "%02d:%02d".format(minutes, seconds)
        }
    }

    fun Context.showToast(@StringRes message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}