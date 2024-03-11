package org.gdglille.devfest.android.theme.m3.style.actions

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

@OptIn(ExperimentalResourceApi::class)
open class TopAction(
    val id: Int,
    val icon: ImageVector,
    val contentDescription: StringResource?
)
