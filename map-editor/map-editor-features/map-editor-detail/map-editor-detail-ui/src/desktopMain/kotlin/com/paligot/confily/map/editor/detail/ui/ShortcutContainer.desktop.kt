package com.paligot.confily.map.editor.detail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun ShortcutContainer(
    onHover: (Boolean) -> Unit,
    modifier: Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .onPointerEvent(PointerEventType.Enter) {
                onHover(true)
            }
            .onPointerEvent(PointerEventType.Exit) {
                onHover(false)
            },
        content = content
    )
}
