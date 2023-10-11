package org.gdglille.devfest.android.theme.m3.style.actions

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class TopActionsUi(
    val actions: ImmutableList<TopAction> = persistentListOf(),
    val maxActions: Int = 3
)
