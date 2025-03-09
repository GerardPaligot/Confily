package com.paligot.confily.map.editor.detail.ui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ShortcutContainer(
    onHover: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
)
