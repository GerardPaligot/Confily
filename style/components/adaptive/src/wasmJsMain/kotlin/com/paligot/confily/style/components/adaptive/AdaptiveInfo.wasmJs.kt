package com.paligot.confily.style.components.adaptive

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.toSize

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
actual fun adaptiveInfo(): WindowSizeClass {
    val windowSize = with(LocalDensity.current) {
        LocalWindowInfo.current.containerSize.toSize().toDpSize()
    }
    return WindowSizeClass.calculateFromSize(windowSize)
}
