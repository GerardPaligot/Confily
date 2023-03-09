package org.gdglille.devfest.android.ui.resources.models

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.ui.resources.actions.TopAction

@Immutable
data class TopActionsUi(
    val actions: ImmutableList<TopAction> = persistentListOf(),
    val maxActions: Int = 3
)
