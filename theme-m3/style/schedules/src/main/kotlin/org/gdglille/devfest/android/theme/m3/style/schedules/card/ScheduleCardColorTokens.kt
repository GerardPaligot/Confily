package org.gdglille.devfest.android.theme.m3.style.schedules.card

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

internal enum class ScheduleCardColorTokens {
    SecondaryColor,
    OnSurfaceColor,
    OnBackgroundColor
}

internal fun ColorScheme.fromToken(value: ScheduleCardColorTokens): Color {
    return when (value) {
        ScheduleCardColorTokens.SecondaryColor -> secondary
        ScheduleCardColorTokens.OnSurfaceColor -> onSurface
        ScheduleCardColorTokens.OnBackgroundColor -> onBackground
    }
}

@Composable
@ReadOnlyComposable
internal fun ScheduleCardColorTokens.toColor(): Color {
    return MaterialTheme.colorScheme.fromToken(this)
}
