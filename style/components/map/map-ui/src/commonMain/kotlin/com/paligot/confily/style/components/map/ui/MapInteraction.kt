package com.paligot.confily.style.components.map.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.paligot.confily.style.components.map.ui.models.PictogramType
import com.paligot.confily.style.components.map.ui.models.PictogramUi
import com.paligot.confily.style.components.map.ui.models.ShapeUi
import com.paligot.confily.style.theme.parseHexColor
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MapInteraction(
    url: String,
    colorHex: String,
    pictoSize: Int,
    shapes: ImmutableList<ShapeUi>,
    pictograms: ImmutableList<PictogramUi>,
    modifier: Modifier = Modifier
) {
    val color = remember(colorHex) { colorHex.parseHexColor() }
    MapBox(url = url, modifier = modifier) {
        val iconSizePx = pictoSize(pictoSize)
        val iconSizeDp = LocalDensity.current.run { iconSizePx.toDp() }
        shapes.forEach { shape ->
            ShapeBox(
                start = this.shapeOffset(shape.start.x, shape.start.y),
                end = this.shapeOffset(shape.end.x, shape.end.y),
                border = BorderStroke(width = 1.dp, color = color)
            )
        }
        pictograms.forEach { pictogram ->
            PictogramIcon(
                imageVector = pictogram.type.toIcon(),
                offset = this.pictoOffset(pictogram.position.x, pictogram.position.y, iconSizePx),
                tint = color,
                modifier = Modifier.size(iconSizeDp)
            )
        }
    }
}

fun PictogramType.toIcon() = when (this) {
    PictogramType.ArrowUp -> Icons.Default.ArrowUpward
    PictogramType.ArrowDown -> Icons.Default.ArrowDownward
    PictogramType.ArrowLeft -> Icons.AutoMirrored.Default.ArrowBack
    PictogramType.ArrowRight -> Icons.AutoMirrored.Default.ArrowForward
    PictogramType.Coffee -> Icons.Default.Coffee
    PictogramType.Restaurant -> Icons.Default.Restaurant
    PictogramType.Parking -> Icons.Default.LocalParking
}
