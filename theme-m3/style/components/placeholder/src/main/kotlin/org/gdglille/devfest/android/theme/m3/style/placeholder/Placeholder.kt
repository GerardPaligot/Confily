package org.gdglille.devfest.android.theme.m3.style.placeholder

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

fun Modifier.placeholder(visible: Boolean) = composed {
    placeholder(
        visible = visible,
        color = MaterialTheme.colorScheme.surface.copy(alpha = .5f),
        highlight = PlaceholderHighlight.shimmer()
    )
}
