package org.gdglille.devfest.android.theme.m3.style.actions

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class NavigationActionsUi(
    val actions: ImmutableList<NavigationAction> = persistentListOf()
)
