package com.paligot.conferences.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

fun Modifier.placeholder(visible: Boolean) = composed {
    val isDark = isSystemInDarkTheme()
    placeholder(
        visible = visible,
        color = if (isDark) MaterialTheme.colors.surface.copy(alpha = .5f) else ColorPalette.grey300,
        highlight = PlaceholderHighlight.shimmer()
    )
}