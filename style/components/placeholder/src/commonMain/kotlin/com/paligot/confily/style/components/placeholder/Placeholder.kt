package com.paligot.confily.style.components.placeholder

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.placeholder
import com.eygraber.compose.placeholder.shimmer

fun Modifier.placeholder(visible: Boolean) = composed {
    then(
        placeholder(
            visible = visible,
            color = MaterialTheme.colorScheme.secondaryContainer,
            highlight = PlaceholderHighlight.shimmer(
                highlightColor = MaterialTheme.colorScheme.background
            )
        )
    )
}
