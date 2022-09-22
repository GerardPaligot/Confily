package org.gdglille.devfest.android.ui.m2.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val lightColors = lightColors(
    primary = ColorPalette.blue500,
    onPrimary = ColorPalette.white,
    secondary = ColorPalette.pink400,
    onSecondary = ColorPalette.white,
    surface = ColorPalette.white,
    onSurface = ColorPalette.black,
    background = ColorPalette.grey50,
    onBackground = ColorPalette.black,
    error = ColorPalette.red,
    onError = ColorPalette.white,
)

val darkColors = darkColors(
    primary = ColorPalette.blue300,
    onPrimary = ColorPalette.black,
    secondary = ColorPalette.pink400,
    onSecondary = ColorPalette.white,
    surface = ColorPalette.grey800,
    onSurface = ColorPalette.white,
    background = ColorPalette.black,
    onBackground = ColorPalette.white,
    error = ColorPalette.red,
    onError = ColorPalette.white,
)

val Colors.onPrimarySurface: Color get() = if (isLight) onPrimary else onSurface
