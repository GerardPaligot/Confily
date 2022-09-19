package org.gdglille.devfest.android.ui.resources.models

import androidx.compose.runtime.Immutable
import org.gdglille.devfest.android.ui.resources.actions.TopAction

@Immutable
data class TopActionsUi(
    val topActions: List<TopAction> = emptyList()
)
