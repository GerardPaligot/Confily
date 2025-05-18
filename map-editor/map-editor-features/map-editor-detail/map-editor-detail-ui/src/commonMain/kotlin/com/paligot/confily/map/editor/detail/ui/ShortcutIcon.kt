package com.paligot.confily.map.editor.detail.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun ShortcutIcon(
    painter: Painter,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    val colors = if (selected) {
        IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    } else {
        IconButtonDefaults.iconButtonColors()
    }
    OutlinedIconButton(
        modifier = modifier,
        onClick = onClick,
        colors = colors
    ) {
        Icon(
            painter = painter,
            contentDescription = null
        )
    }
}
