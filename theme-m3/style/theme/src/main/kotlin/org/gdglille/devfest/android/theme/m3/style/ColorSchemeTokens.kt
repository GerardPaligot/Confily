package org.gdglille.devfest.android.theme.m3.style

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

enum class ColorSchemeTokens {
    PrimaryColor,
    SecondaryColor,
    SurfaceColor,
    OnSurfaceColor,
    OnBackgroundColor
}

internal fun ColorScheme.fromToken(value: ColorSchemeTokens): Color {
    return when (value) {
        ColorSchemeTokens.PrimaryColor -> primary
        ColorSchemeTokens.SecondaryColor -> secondary
        ColorSchemeTokens.SurfaceColor -> surface
        ColorSchemeTokens.OnSurfaceColor -> onSurface
        ColorSchemeTokens.OnBackgroundColor -> onBackground
    }
}

@Composable
@ReadOnlyComposable
fun ColorSchemeTokens.toColor(): Color {
    return MaterialTheme.colorScheme.fromToken(this)
}
