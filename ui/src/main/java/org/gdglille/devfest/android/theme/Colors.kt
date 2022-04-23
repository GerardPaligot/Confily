package org.gdglille.devfest.android.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

object ColorPalette {
    val black = Color(0xFF191919)
    val grey800 = Color(0xFF393939)
    val grey700 = Color(0xFF575757)
    val grey600 = Color(0xFF6b6b6b)
    val grey500 = Color(0xFF939393)
    val grey400 = Color(0xFFb3b3b3)
    val grey300 = Color(0xFFd7d7d7)
    val grey200 = Color(0xFFe7e7e7)
    val grey100 = Color(0xFFf1f1f1)
    val grey50 = Color(0xFFf8f8f8)
    val white = Color(0xFFFFFFFF)

    val blue900 = Color(0xFF005b64)
    val blue800 = Color(0xFF007e8f)
    val blue700 = Color(0xFF0091a8)
    val blue600 = Color(0xFF00a6c2)
    val blue500 = Color(0xFF03B7D5)
    val blue400 = Color(0xFF24C0DC)
    val blue300 = Color(0xFF4bcbe4)
    val blue200 = Color(0xFF7fdaed)
    val blue100 = Color(0xFFb1e9f4)
    val blue50 = Color(0xFFe0f6fb)

    val red = Color(0xFFB21414)
    val orange = Color(0xFFEF7918)

    val pink900 = Color(0xFF810050)
    val pink800 = Color(0xFFa80058)
    val pink700 = Color(0xFFbd005c)
    val pink600 = Color(0xFFd30062)
    val pink500 = Color(0xFFe40065)
    val pink400 = Color(0xFFE6147C)
    val pink300 = Color(0xFFe94e95)
    val pink200 = Color(0xFFee84b4)
    val pink100 = Color(0xFFf4b5d2)
    val pink50 = Color(0xFFfbe2ed)
}

val lightColors = lightColors(
    primary = ColorPalette.blue500,
    onPrimary = ColorPalette.white,
    secondary = ColorPalette.pink400,
    onSecondary = ColorPalette.black,
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
