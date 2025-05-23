package com.paligot.confily.map.editor.detail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun MapBox(
    onPress: (Offset) -> Unit,
    onRelease: (Offset) -> Unit,
    onMove: (Offset, Boolean) -> Unit,
    modifier: Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .onPointerEvent(PointerEventType.Press) {
                onPress(it.changes.first().position)
            }
            .onPointerEvent(PointerEventType.Release) {
                onRelease(it.changes.first().position)
            }
            .onPointerEvent(PointerEventType.Move) {
                onMove(it.changes.first().position, it.changes.first().pressed)
            },
        content = content
    )
}
