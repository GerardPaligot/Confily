package com.paligot.confily.infos.panes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.paligot.confily.infos.ui.models.MapItemUiState
import com.paligot.confily.style.components.map.ui.MapBox
import com.paligot.confily.style.components.map.ui.PictogramIcon
import com.paligot.confily.style.components.map.ui.ShapeBox
import com.paligot.confily.style.components.map.ui.toIcon
import com.paligot.confily.style.theme.parseHexColor

@Composable
fun MapItemListContent(
    uiState: MapItemUiState,
    modifier: Modifier = Modifier,
    onFilterClick: (String, String) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 24.dp, bottom = 72.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(uiState.uiModel.items) { item ->
            val state = rememberLazyListState()
            val color = remember(item.colorHex) { item.colorHex.parseHexColor() }
            val selectedColor =
                remember(item.selectedColorHex) { item.selectedColorHex.parseHexColor() }
            val label = uiState.labelling[item.id]?.label
            val description = uiState.labelling[item.id]?.description
            LaunchedEffect(label) {
                val index = item.labels.indexOf(label)
                if (index != -1) {
                    state.animateScrollToItem(index)
                }
            }
            Column {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                LazyRow(
                    state = state,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(item.labels) {
                        val selected = it == label
                        FilterChip(
                            selected = selected,
                            onClick = { onFilterClick(item.id, it) },
                            label = { Text(text = it) },
                            modifier = Modifier.clearAndSetSemantics {
                                role = Role.Checkbox
                                this.selected = selected
                                text = AnnotatedString(it)
                                stateDescription = if (selected && description != null) {
                                    description
                                } else {
                                    it
                                }
                            }
                        )
                    }
                }
                MapBox(url = item.url, modifier = Modifier.fillMaxSize().clearAndSetSemantics { }) {
                    val iconSizePx = pictoSize(item.pictoSize)
                    val iconSizeDp = LocalDensity.current.run { iconSizePx.toDp() }
                    item.shapes.forEach {
                        val borderColor = if (it.label == label) {
                            selectedColor
                        } else {
                            color
                        }
                        val background = if (it.label == label) {
                            selectedColor.copy(alpha = .1f)
                        } else {
                            Color.Transparent
                        }
                        ShapeBox(
                            start = this.shapeOffset(it.start.x, it.start.y),
                            end = this.shapeOffset(it.end.x, it.end.y),
                            border = BorderStroke(1.dp, borderColor),
                            modifier = Modifier
                                .clickable(enabled = it.label != "") {
                                    onFilterClick(item.id, it.label)
                                }
                                .background(background)
                        )
                    }
                    item.pictograms.forEach {
                        val tintColor = if (it.label == label) {
                            selectedColor
                        } else {
                            color
                        }
                        val borderColor = if (it.label == label && label != "") {
                            selectedColor
                        } else {
                            Color.Transparent
                        }
                        PictogramIcon(
                            imageVector = it.type.toIcon(),
                            offset = this.pictoOffset(it.position.x, it.position.y, iconSizePx),
                            tint = tintColor,
                            modifier = Modifier
                                .size(iconSizeDp)
                                .clickable(enabled = it.label != "") {
                                    onFilterClick(item.id, it.label)
                                }
                                .border(1.dp, borderColor)
                        )
                    }
                }
                if (description != null) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 8.dp).clearAndSetSemantics { }
                    )
                }
            }
        }
    }
}
