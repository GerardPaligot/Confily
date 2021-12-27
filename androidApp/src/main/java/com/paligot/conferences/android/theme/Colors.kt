package com.paligot.conferences.android.theme

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

object ColorPalette {
    val black = Color(0xFF191919)
    val white = Color(0xFFFFFFFF)
    val blue = Color(0xFF03B7D5)
    val red = Color(0xFFB21414)
    val orange = Color(0xFFEF7918)
    val pink = Color(0xFFE6147C)
}

val darkColors = lightColors(
    primary = ColorPalette.pink,
    primaryVariant = ColorPalette.orange,
    onPrimary = ColorPalette.black,
    secondary = ColorPalette.blue,
    onSecondary = ColorPalette.black,
    background = ColorPalette.black,
    onBackground = ColorPalette.white,
    surface = ColorPalette.black,
    onSurface = ColorPalette.white,
    error = ColorPalette.red,
    onError = ColorPalette.white,
)
