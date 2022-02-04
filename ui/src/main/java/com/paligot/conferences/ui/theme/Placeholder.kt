package com.paligot.conferences.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

fun Modifier.placeholder(visible: Boolean) = composed {
    placeholder(
        visible = visible,
        color = MaterialTheme.colors.surface.copy(alpha = .5f),
        highlight = PlaceholderHighlight.shimmer()
    )
}