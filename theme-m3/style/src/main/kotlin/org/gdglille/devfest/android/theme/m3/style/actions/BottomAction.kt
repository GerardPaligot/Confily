package org.gdglille.devfest.android.theme.m3.style.actions

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

open class BottomAction(
    val route: String,
    val icon: ImageVector,
    val iconSelected: ImageVector,
    @StringRes val label: Int,
    @StringRes val contentDescription: Int?
)
