package org.gdglille.devfest.android.theme.vitamin.ui.theme

import android.view.accessibility.AccessibilityManager
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAccessibility = staticCompositionLocalOf<AccessibilityManager> {
    error("CompositionLocal LocalAccessibility not present")
}
