package com.paligot.confily.style.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun colorScheme(useDarkTheme: Boolean): ColorScheme =
    if (useDarkTheme) DarkColors else LightColors
