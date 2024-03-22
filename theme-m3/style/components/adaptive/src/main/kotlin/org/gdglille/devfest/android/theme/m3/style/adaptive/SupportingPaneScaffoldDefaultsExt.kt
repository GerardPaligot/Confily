package org.gdglille.devfest.android.theme.m3.style.adaptive

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.SupportingPaneScaffoldDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable

/**
 * By default, supporting page scaffold configure a window inset as if it was used
 * without NavigationSuiteScaffold component and add a vertical padding.
 * This padding is useful when we display more than one pane on the screen but when
 * we are in compact mode, we don't want any vertical padding.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
val SupportingPaneScaffoldDefaults.windowInsetsAdaptive: WindowInsets
    @Composable
    get() {
        val windowSizeClass = currentWindowAdaptiveInfo()
        return if (windowSizeClass.windowSizeClass.widthSizeClass.isCompat) {
            WindowInsets(0, 0, 0, 0)
        } else {
            windowInsets
        }
    }
