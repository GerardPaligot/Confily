package org.gdglille.devfest.android.ui.m2.theme

import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

@Composable
fun Conferences4HallTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAccessibility provides
            LocalContext.current.getSystemService(Context.ACCESSIBILITY_SERVICE)
            as AccessibilityManager
    ) {
        MaterialTheme(
            colors = if (isDarkMode) darkColors else lightColors,
            typography = typography,
            content = content
        )
    }
}
