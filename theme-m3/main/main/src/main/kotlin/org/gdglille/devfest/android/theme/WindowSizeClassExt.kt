package org.gdglille.devfest.android.theme

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

val WindowWidthSizeClass.isCompat: Boolean
    get() = this == WindowWidthSizeClass.Compact

val WindowWidthSizeClass.isMedium: Boolean
    get() = this == WindowWidthSizeClass.Medium

val WindowWidthSizeClass.isExpanded: Boolean
    get() = this == WindowWidthSizeClass.Expanded

val WindowHeightSizeClass.isCompat: Boolean
    get() = this == WindowHeightSizeClass.Compact

val WindowHeightSizeClass.isMedium: Boolean
    get() = this == WindowHeightSizeClass.Medium
