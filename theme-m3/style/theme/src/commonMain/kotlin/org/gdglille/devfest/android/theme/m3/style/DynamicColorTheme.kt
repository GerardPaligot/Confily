package org.gdglille.devfest.android.theme.m3.style

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
expect fun colorScheme(useDarkTheme: Boolean = isSystemInDarkTheme()): ColorScheme
