package com.abi.musicplayer.ui.screens.musicPlayerScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abi.musicplayer.R

@Composable
fun AudioGraphView(modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val totalCount = 100
        val itemWidth = (this.maxWidth / totalCount)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            repeat(times = totalCount) {
                val randomHeight = (0..totalCount).random()
                Box(
                    modifier = Modifier
                        .width(width = itemWidth)
                        .height(height = randomHeight.dp)
                        .background(color = colorResource(id = R.color.white).copy(alpha = 0.2F))
                )
            }
        }
    }
}

@Preview
@Composable
fun AudioGraphPreview() {
    AudioGraphView(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 100.dp)
    )
}