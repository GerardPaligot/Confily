package com.paligot.confily.style.theme.actions

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource

open class TopAction(
    val id: Int,
    val icon: ImageVector,
    val contentDescription: StringResource?
)
