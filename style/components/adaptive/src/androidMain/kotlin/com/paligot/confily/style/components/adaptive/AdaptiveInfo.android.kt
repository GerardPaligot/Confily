package com.paligot.confily.style.components.adaptive

import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
actual fun adaptiveInfo(): WindowSizeClass {
    val windowSize = with(LocalDensity.current) { currentWindowSize().toSize().toDpSize() }
    return WindowSizeClass.calculateFromSize(windowSize)
}
