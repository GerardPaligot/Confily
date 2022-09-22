package org.gdglille.devfest.android.ui.resources.models

import androidx.compose.runtime.Immutable
import org.gdglille.devfest.android.ui.resources.actions.BottomAction

@Immutable
data class BottomActionsUi(
    val actions: List<BottomAction> = emptyList()
)
