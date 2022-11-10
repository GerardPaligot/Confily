package org.gdglille.devfest.android.theme

import android.view.accessibility.AccessibilityManager
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAccessibility = staticCompositionLocalOf<AccessibilityManager> {
    error("CompositionLocal LocalAccessibility not present")
}

val LocalDecorativeColorScheme = staticCompositionLocalOf<DecorativeColorScheme> {
    error("CompositionLocal LocalDecorativeColors not present")
}
