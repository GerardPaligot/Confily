package com.paligot.confily.style.components.map.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import coil3.compose.rememberAsyncImagePainter

/** A BoxScope provides a scope for the children of [Box] and [BoxWithConstraints]. */
@LayoutScopeMarker
@Immutable
interface MapBoxScope {
    @Stable
    fun shapeOffset(x: Float, y: Float): IntOffset

    @Stable
    fun pictoSize(size: Int): Float

    @Stable
    fun pictoOffset(x: Float, y: Float, size: Float): IntOffset
}

internal class MapBoxInstance(private val containerSize: IntSize) : MapBoxScope {
    override fun shapeOffset(x: Float, y: Float): IntOffset = IntOffset(
        x = (x / 100 * containerSize.width).toInt(),
        y = (y / 100 * containerSize.height).toInt()
    )

    override fun pictoSize(size: Int): Float = size / 100f * containerSize.height

    override fun pictoOffset(x: Float, y: Float, size: Float): IntOffset {
        return IntOffset(
            x = ((x / 100 * containerSize.width) - (size / 2)).toInt(),
            y = ((y / 100 * containerSize.height) - (size / 2)).toInt()
        )
    }
}

@Composable
fun MapBox(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillWidth,
    content: @Composable MapBoxScope.() -> Unit
) {
    var size by remember { mutableStateOf(IntSize(0, 0)) }
    Box(
        modifier = modifier
            .clipToBounds()
            .paint(
                painter = rememberAsyncImagePainter(model = url, contentScale = contentScale),
                contentScale = contentScale
            )
            .onGloballyPositioned { coordinates ->
                size = coordinates.size
            },
        content = { MapBoxInstance(size).content() }
    )
}
