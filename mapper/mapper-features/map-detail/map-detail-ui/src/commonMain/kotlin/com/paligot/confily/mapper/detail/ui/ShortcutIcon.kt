package com.paligot.confily.mapper.detail.ui

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
    IconButton(
        modifier = modifier,
        onClick = onClick,
        colors = colors,
        shape = IconButtonDefaults.smallSquareShape
    ) {
        Icon(
            painter = painter,
            contentDescription = null
        )
    }
}
