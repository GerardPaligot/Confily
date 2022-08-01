package org.gdglille.devfest.android.theme.vitamin.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

fun Modifier.placeholder(visible: Boolean) = composed {
    placeholder(
        visible = visible,
        color = VitaminTheme.colors.vtmnBackgroundTertiary,
        highlight = PlaceholderHighlight.shimmer()
    )
}
