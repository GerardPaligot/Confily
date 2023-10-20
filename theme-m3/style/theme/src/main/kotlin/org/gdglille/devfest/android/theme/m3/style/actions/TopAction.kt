package org.gdglille.devfest.android.theme.m3.style.actions

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

open class TopAction(
    val id: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int?
)
