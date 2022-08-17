package org.gdglille.devfest.android.ui.resources.models

import androidx.compose.runtime.Immutable
import org.gdglille.devfest.android.ui.resources.actions.TabAction

@Immutable
data class TabActionsUi(
    val scrollable: Boolean = false,
    val tabActions: List<TabAction> = emptyList()
)
