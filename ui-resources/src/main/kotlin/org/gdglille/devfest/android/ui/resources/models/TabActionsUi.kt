package org.gdglille.devfest.android.ui.resources.models

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.ui.resources.actions.TabAction

@Immutable
data class TabActionsUi(
    val scrollable: Boolean = false,
    val actions: ImmutableList<TabAction> = persistentListOf()
)
