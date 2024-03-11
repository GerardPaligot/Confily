package org.gdglille.devfest.android.theme.m3.style.actions

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class TabActionsUi(
    val scrollable: Boolean = false,
    val actions: ImmutableList<TabAction> = persistentListOf()
)
