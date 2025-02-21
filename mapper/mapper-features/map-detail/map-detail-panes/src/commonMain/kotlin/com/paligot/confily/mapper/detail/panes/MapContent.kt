package com.paligot.confily.mapper.detail.panes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.paligot.confily.mapper.detail.ui.MapBox
import com.paligot.confily.mapper.detail.ui.models.MappingUi
import com.paligot.confily.style.components.map.ui.PictogramIcon
import com.paligot.confily.style.components.map.ui.ShapeBox
import com.paligot.confily.style.components.map.ui.toIcon
import com.paligot.confily.style.theme.parseHexColor

@Composable
fun MapContent(
    uiModel: MappingUi,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillWidth,
    onPress: (Offset) -> Unit,
    onRelease: (Offset) -> Unit,
    onMove: (Offset, Boolean) -> Unit
) {
    var size by remember { mutableStateOf(IntSize(0, 0)) }
    val color = remember(uiModel.color) { uiModel.color.parseHexColor() }
    val selectedColor = remember(uiModel.color) { uiModel.selectedColor.parseHexColor() }
    MapBox(
        modifier = modifier
            .fillMaxWidth()
            .clipToBounds()
            .paint(
                painter = rememberAsyncImagePainter(
                    model = uiModel.planUrl,
                    contentScale = contentScale
                ),
                contentScale = contentScale
            )
            .border(2.dp, Color.Black)
            .onGloballyPositioned { coordinates ->
                size = coordinates.size
            },
        onPress = {
            onPress(it.copy(x = it.x / size.width * 100, y = it.y / size.height * 100))
        },
        onRelease = {
            onRelease(it.copy(x = it.x / size.width * 100, y = it.y / size.height * 100))
        },
        onMove = { it, pressed ->
            onMove(it.copy(x = it.x / size.width * 100, y = it.y / size.height * 100), pressed)
        }
    ) {
        val iconSizePx = uiModel.pictoSize / 100f * size.height
        val iconSizeDp = LocalDensity.current.run { iconSizePx.toDp() }
        if (uiModel.started) {
            ShapeBox(
                start = IntOffset(
                    x = (uiModel.start.x / 100 * size.width).toInt(),
                    y = (uiModel.start.y / 100 * size.height).toInt()
                ),
                end = IntOffset(
                    x = (uiModel.end.x / 100 * size.width).toInt(),
                    y = (uiModel.end.y / 100 * size.height).toInt()
                ),
                border = BorderStroke(width = 2.dp, color = selectedColor)
            )
        }
        uiModel.shapes.forEach {
            val borderColor = if (it.label == uiModel.labelSelected) selectedColor else color
            val containerColor = if (it.label == uiModel.labelSelected) selectedColor.copy(alpha = 0.3f) else Color.Transparent
            ShapeBox(
                start = IntOffset(
                    x = (it.start.x / 100 * size.width).toInt(),
                    y = (it.start.y / 100 * size.height).toInt()
                ),
                end = IntOffset(
                    x = (it.end.x / 100 * size.width).toInt(),
                    y = (it.end.y / 100 * size.height).toInt()
                ),
                border = BorderStroke(width = 2.dp, color = borderColor),
                modifier = Modifier.background(containerColor)
            )
        }
        uiModel.pictograms.forEach {
            val borderColor = if (it.label == uiModel.labelSelected) selectedColor else Color.Transparent
            val tintColor = if (it.label == uiModel.labelSelected) selectedColor else color
            PictogramIcon(
                imageVector = it.type.toIcon(),
                offset = IntOffset(
                    x = ((it.position.x / 100 * size.width) - (iconSizePx / 2)).toInt(),
                    y = ((it.position.y / 100 * size.height) - (iconSizePx / 2)).toInt()
                ),
                tint = tintColor,
                modifier = Modifier.size(iconSizeDp).border(width = 2.dp, color = borderColor)
            )
        }
    }
}
