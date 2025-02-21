package com.paligot.confily.mapper.detail.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.paligot.confily.mapper.detail.ui.ColorPickerTextField
import com.paligot.confily.mapper.detail.ui.PictogramItem
import com.paligot.confily.mapper.detail.ui.models.ControllerUi
import com.paligot.confily.mapper.detail.ui.models.MappingMode
import com.paligot.confily.mapper.detail.ui.models.MappingUi
import com.paligot.confily.mapper.list.ui.SurfacePickFile
import com.paligot.confily.style.components.map.ui.MapInteraction
import com.paligot.confily.style.components.map.ui.models.MappingType
import com.paligot.confily.style.components.map.ui.models.PictogramType

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ControllerContent(
    uiModel: ControllerUi,
    uiInteractionModel: MappingUi?,
    onPickEmptyFiles: (List<String>) -> Unit,
    onPickFilledFiles: (List<String>) -> Unit,
    onNameValueChange: (String) -> Unit,
    onColorValueChange: (String) -> Unit,
    onColorSelectedValueChange: (String) -> Unit,
    onShapeOrderValueChange: (String) -> Unit,
    onShapeValueChange: (String) -> Unit,
    onDescriptionValueChange: (String) -> Unit,
    onPictogramSizeValueChange: (String) -> Unit,
    onTypeClick: (MappingType) -> Unit,
    onPictogramClick: (PictogramType) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            Button(
                enabled = enabled,
                onClick = onSaveClick,
                shape = ButtonDefaults.squareShape
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null
                    )
                    Text(text = "Save")
                }
            }
        }
    ) {
        LazyColumn(
            contentPadding = it,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                OutlinedTextField(
                    value = uiModel.name,
                    onValueChange = onNameValueChange,
                    label = { Text(text = "Name") },
                    enabled = enabled
                )
            }
            item {
                ColorPickerTextField(
                    value = uiModel.color,
                    onValueChange = onColorValueChange,
                    label = { Text(text = "Color") },
                    enabled = enabled
                )
            }
            item {
                ColorPickerTextField(
                    value = uiModel.colorSelected,
                    onValueChange = onColorSelectedValueChange,
                    label = { Text(text = "Selected color") },
                    enabled = enabled
                )
            }
            when (uiModel.modeSelected) {
                MappingMode.Draw -> {
                    stickyHeader { Text(text = "Type") }
                    item {
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            uiModel.types.forEach { type ->
                                FilterChip(
                                    selected = uiModel.typeSelected == type,
                                    onClick = { onTypeClick(type) },
                                    label = { Text(text = type.name) },
                                    enabled = enabled
                                )
                            }
                        }
                    }
                }
                MappingMode.Selection -> {
                    if (uiModel.shapeName != null) {
                        item {
                            OutlinedTextField(
                                value = uiModel.shapeOrder ?: "",
                                onValueChange = onShapeOrderValueChange,
                                label = { Text(text = "Shape order") },
                                enabled = enabled
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = uiModel.shapeName!!,
                                onValueChange = onShapeValueChange,
                                label = { Text(text = "Shape name") },
                                enabled = enabled
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = uiModel.shapeDescription ?: "",
                                onValueChange = onDescriptionValueChange,
                                label = { Text(text = "Shape description") },
                                enabled = enabled
                            )
                        }
                    }
                }
                MappingMode.Pictogram -> {
                    stickyHeader { Text(text = "Pictogram selection") }
                    item {
                        OutlinedTextField(
                            value = uiModel.pictogramSize,
                            onValueChange = onPictogramSizeValueChange,
                            label = { Text(text = "Pictogram size") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            enabled = enabled
                        )
                    }
                    item {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            itemVerticalAlignment = Alignment.CenterVertically
                        ) {
                            PictogramType.entries.forEach { pictogram ->
                                PictogramItem(
                                    painter = rememberVectorPainter(
                                        image = when (pictogram) {
                                            PictogramType.ArrowUp -> Icons.Default.ArrowUpward
                                            PictogramType.ArrowDown -> Icons.Default.ArrowDownward
                                            PictogramType.ArrowLeft -> Icons.AutoMirrored.Default.ArrowBack
                                            PictogramType.ArrowRight -> Icons.AutoMirrored.Default.ArrowForward
                                            PictogramType.Coffee -> Icons.Default.Coffee
                                            PictogramType.Restaurant -> Icons.Default.Restaurant
                                            PictogramType.Parking -> Icons.Default.LocalParking
                                        }
                                    ),
                                    selected = uiModel.pictogramSelected == pictogram,
                                    onClick = { onPictogramClick(pictogram) },
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                    }
                }

                else -> {
                    // No additional content
                }
            }
            if (uiInteractionModel != null) {
                item {
                    MapInteraction(
                        url = uiInteractionModel.planUrl,
                        colorHex = uiInteractionModel.color,
                        pictoSize = uiInteractionModel.pictoSize,
                        shapes = uiInteractionModel.shapes,
                        pictograms = uiInteractionModel.pictograms,
                        modifier = Modifier.width(350.dp)
                    )
                }
            }
            item {
                SurfacePickFile(
                    label = "Drag and drop an empty map file",
                    onPickFiles = onPickEmptyFiles,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }
            item {
                SurfacePickFile(
                    label = "Drag and drop filled map file",
                    onPickFiles = onPickFilledFiles,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }
        }
    }
}
