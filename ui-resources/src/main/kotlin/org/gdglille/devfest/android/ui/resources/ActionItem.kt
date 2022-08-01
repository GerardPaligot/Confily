package org.gdglille.devfest.android.ui.resources

import androidx.compose.ui.graphics.vector.ImageVector

open class ActionItem(
    val id: ActionItemId,
    val icon: ImageVector,
    val contentDescription: Int?,
    val formatArgs: List<String> = emptyList()
)
