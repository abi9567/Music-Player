package com.abi.musicplayer.ui.screens.musicPlayerScreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abi.musicplayer.R

@Composable
fun AudioGraphView(
    modifier: Modifier = Modifier,
    isPlaying: State<Boolean>
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {

        val infiniteAnimation = rememberInfiniteTransition()
        val totalCount = 100
        val itemWidth = (this.maxWidth / totalCount)

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            repeat(times = totalCount) {
                val randomHeight = remember { (0..totalCount).random() }
                val animatedHeight by infiniteAnimation.animateValue(
                    initialValue = 0.dp,
                    targetValue = randomHeight.dp,
                    typeConverter = Dp.VectorConverter,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 1000,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Reverse
                    ), label = "Bar Height"
                )

                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(size = 8.dp))
                        .align(alignment = Alignment.Bottom)
                        .width(width = itemWidth)
                        .height(height = if (!isPlaying.value) randomHeight.dp else animatedHeight)
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
            .height(height = 100.dp),
        isPlaying = remember { mutableStateOf(value = true) }
    )
}