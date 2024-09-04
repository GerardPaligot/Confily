package com.paligot.confily.style.theme.actions

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource

open class NavigationAction(
    val route: String,
    val icon: ImageVector,
    val iconSelected: ImageVector,
    val label: StringResource
)
