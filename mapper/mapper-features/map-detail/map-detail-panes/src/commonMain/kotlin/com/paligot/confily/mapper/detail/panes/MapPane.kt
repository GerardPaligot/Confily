package com.paligot.confily.mapper.detail.panes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.paligot.confily.mapper.detail.ui.models.MapDetailUiState
import com.paligot.confily.mapper.detail.ui.models.MappingMode
import com.paligot.confily.style.components.map.ui.models.MappingType
import com.paligot.confily.style.components.map.ui.models.PictogramType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPane(
    uiState: MapDetailUiState,
    modifier: Modifier = Modifier,
    onPress: (Offset) -> Unit,
    onRelease: (Offset) -> Unit,
    onMove: (Offset, Boolean) -> Unit,
    onPickEmptyFiles: (List<String>) -> Unit,
    onPickFilledFiles: (List<String>) -> Unit,
    onNameValueChange: (String) -> Unit,
    onColorValueChange: (String) -> Unit,
    onColorSelectedValueChange: (String) -> Unit,
    onShapeOrderValueChange: (String) -> Unit,
    onShapeValueChange: (String) -> Unit,
    onDescriptionValueChange: (String) -> Unit,
    onPictogramSizeValueChange: (String) -> Unit,
    onModeClick: (MappingMode) -> Unit,
    onTypeClick: (MappingType) -> Unit,
    onPictogramClick: (PictogramType) -> Unit,
    onCloseClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = uiState.name) },
                actions = {
                    IconButton(onClick = onCloseClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        if (uiState.mapping != null) {
            Row(modifier = Modifier.padding(it)) {
                ShortcutContent(
                    selected = uiState.controller.modeSelected,
                    onClick = onModeClick,
                    modifier = Modifier.fillMaxHeight()
                )
                LazyColumn(modifier = Modifier.fillMaxSize().weight(1f)) {
                    item {
                        MapContent(
                            uiModel = uiState.mapping!!,
                            onPress = onPress,
                            onRelease = onRelease,
                            onMove = onMove
                        )
                    }
                    if (uiState.filledMapUrl != null) {
                        item {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = uiState.filledMapUrl!!,
                                    contentScale = ContentScale.FillWidth
                                ),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.fillMaxWidth().clipToBounds()
                            )
                        }
                    }
                }
                ControllerContent(
                    uiModel = uiState.controller,
                    uiInteractionModel = uiState.mapping,
                    onPickEmptyFiles = onPickEmptyFiles,
                    onPickFilledFiles = onPickFilledFiles,
                    onNameValueChange = onNameValueChange,
                    onColorValueChange = onColorValueChange,
                    onColorSelectedValueChange = onColorSelectedValueChange,
                    onShapeOrderValueChange = onShapeOrderValueChange,
                    onShapeValueChange = onShapeValueChange,
                    onDescriptionValueChange = onDescriptionValueChange,
                    onPictogramSizeValueChange = onPictogramSizeValueChange,
                    onTypeClick = onTypeClick,
                    onPictogramClick = onPictogramClick,
                    onSaveClick = onSaveClick,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(.3f),
                    enabled = uiState.enabled
                )
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
