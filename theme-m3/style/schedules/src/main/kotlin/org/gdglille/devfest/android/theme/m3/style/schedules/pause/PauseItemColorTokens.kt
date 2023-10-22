package org.gdglille.devfest.android.theme.m3.style.schedules.pause

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

internal enum class PauseItemColorTokens {
    SurfaceColor
}

internal fun ColorScheme.fromToken(value: PauseItemColorTokens): Color {
    return when (value) {
        PauseItemColorTokens.SurfaceColor -> surface
    }
}

@Composable
@ReadOnlyComposable
internal fun PauseItemColorTokens.toColor(): Color {
    return MaterialTheme.colorScheme.fromToken(this)
}
