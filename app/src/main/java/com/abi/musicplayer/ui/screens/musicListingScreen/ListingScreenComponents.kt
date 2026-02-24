package com.abi.musicplayer.ui.screens.musicListingScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.abi.musicplayer.R
import com.abi.musicplayer.data.model.AudioFile
import com.abi.musicplayer.utils.Utils.formatMillis

@Composable
fun ListingSingleItem(
    music : AudioFile,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
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
        Spacer(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.margin_normal))
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.margin_small))
        ) {
            Text(
                text = music.fileName,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(id = R.color.white),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = music.artistName,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelSmall,
                color = colorResource(id = R.color.white),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = music.totalDuration.formatMillis(),
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(id = R.color.white),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListingSingleItemView() {
    ListingSingleItem(
        music = AudioFile(
            id = 1,
            fileName = "Vettam",
            thumbnail = null,
            artistName = "M G Sreekumar",
            totalDuration = 1000L
        ), onClick = {}
    )
}