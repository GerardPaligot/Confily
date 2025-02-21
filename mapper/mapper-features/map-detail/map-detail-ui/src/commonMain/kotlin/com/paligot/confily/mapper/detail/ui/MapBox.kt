package com.paligot.confily.mapper.detail.ui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset

@Composable
expect fun MapBox(
    onPress: (Offset) -> Unit,
    onRelease: (Offset) -> Unit,
    onMove: (Offset, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
)
