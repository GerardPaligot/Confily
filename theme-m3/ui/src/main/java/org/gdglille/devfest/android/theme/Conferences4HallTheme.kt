package org.gdglille.devfest.android.theme

import android.content.Context
import android.os.Build
import android.view.accessibility.AccessibilityManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

@Composable
fun Conferences4HallTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAccessibility provides
            LocalContext.current.getSystemService(Context.ACCESSIBILITY_SERVICE)
            as AccessibilityManager,
        LocalDecorativeColorScheme provides
            if (useDarkTheme) darkDecorativeColorScheme()
            else lightDecorativeColorScheme()
    ) {
        val colorSheme = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (useDarkTheme) dynamicDarkColorScheme(context)
                else dynamicLightColorScheme(context)
            }

            else -> if (useDarkTheme) DarkColors else LightColors
        }
        MaterialTheme(
            colorScheme = colorSheme,
            typography = typography,
            content = content
        )
    }
}
