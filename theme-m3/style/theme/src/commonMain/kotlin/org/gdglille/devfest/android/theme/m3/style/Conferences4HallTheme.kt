package org.gdglille.devfest.android.theme.m3.style

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun Conferences4HallTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme = colorScheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalDecorativeColorScheme provides
            if (useDarkTheme) {
                darkDecorativeColorScheme()
            } else {
                lightDecorativeColorScheme()
            }
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography(),
            content = content
        )
    }
}
