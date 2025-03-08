package com.paligot.confily.style.components.map.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun ShapeBox(
    start: IntOffset,
    end: IntOffset,
    modifier: Modifier = Modifier,
    border: BorderStroke = BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimaryContainer)
) {
    Box(
        modifier = Modifier
            .offset { start }
            .size(
                width = (end.x - start.x).toDp,
                height = (end.y - start.y).toDp
            )
            .then(modifier)
            .border(border)
    )
}

inline val Int.toDp: Dp
    @Composable get() = with(LocalDensity.current) { this@toDp.toDp() }
