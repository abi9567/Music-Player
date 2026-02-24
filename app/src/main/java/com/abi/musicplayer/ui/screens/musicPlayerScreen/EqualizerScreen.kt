package com.abi.musicplayer.ui.screens.musicPlayerScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abi.musicplayer.R
import com.abi.musicplayer.utils.Utils.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerScreen(
    viewModel: MusicPlayerViewModel,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val audioEffect by viewModel.audioEffect.collectAsStateWithLifecycle()
    val selectedPreset by viewModel.selectedPreset.collectAsStateWithLifecycle()
    var isDropdownExpanded by remember { mutableStateOf(value = false) }

    ModalBottomSheet(
        sheetState = state,
        onDismissRequest = onDismissRequest
    ) {

        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }
        ) {
            audioEffect?.presets?.forEachIndexed { index, name ->
                DropdownMenuItem(
                    text = {
                        Text(name)
                    },
                    onClick = {
                        viewModel.setPreset(preset = index, name = name)
                        isDropdownExpanded = false
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(all = dimensionResource(id = R.dimen.margin_large))
                .fillMaxWidth(),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (selectedPreset.isNullOrEmpty()) stringResource(R.string.select_preset)
                    else stringResource(id = R.string.selected_preset, "$selectedPreset")
                )
                Spacer(
                    modifier = Modifier.height(height = dimensionResource(id = R.dimen.margin_normal))
                )
                if (selectedPreset != null) {
                    IconButton(onClick = viewModel::resetPreset) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            tint = colorResource(id = R.color.background_color),
                            contentDescription = null
                        )
                    }
                }
                IconButton(
                    onClick = { isDropdownExpanded = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        tint = colorResource(id = R.color.background_color),
                        contentDescription = null
                    )
                }
            }

            audioEffect?.bandLevels?.forEachIndexed { index, level ->
                Row(
                    modifier = Modifier
                        .padding(top = dimensionResource(id = R.dimen.margin_large))
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(id = R.string.band, "${index + 1}"))
                    Spacer(
                        modifier = Modifier.width(width = dimensionResource(id = R.dimen.margin_normal))
                    )
                    Slider(
                        value = level.toFloat(),
                        enabled = selectedPreset == null,
                        onValueChange = {
                            viewModel.setBandLevel(index, it)
                        },
                        valueRange = (audioEffect?.minLevel ?: 0).toFloat()..(audioEffect?.maxLevel ?: 0).toFloat(),
                        modifier = Modifier
                            .clickable {
                                if (selectedPreset != null) {
                                    context.showToast(message = R.string.reset_preset_message)
                                }
                            }
                            .fillMaxWidth()
                            .weight(weight = 1F)
                    )
                }
            }
        }
    }
}