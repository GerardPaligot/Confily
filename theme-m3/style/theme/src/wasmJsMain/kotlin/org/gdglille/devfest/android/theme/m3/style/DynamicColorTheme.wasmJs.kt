package org.gdglille.devfest.android.theme.m3.style

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun colorScheme(useDarkTheme: Boolean): ColorScheme =
    if (useDarkTheme) DarkColors else LightColors
