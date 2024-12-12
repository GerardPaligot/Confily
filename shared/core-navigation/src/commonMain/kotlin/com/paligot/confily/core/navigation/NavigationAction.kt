package com.paligot.confily.core.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource

open class NavigationAction<T : NavigationBar>(
    val route: T,
    val icon: ImageVector,
    val iconSelected: ImageVector,
    val label: StringResource
)
