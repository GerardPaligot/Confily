package com.paligot.confily.style.theme.actions

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class NavigationActionsUi(
    val actions: ImmutableList<NavigationAction> = persistentListOf()
)
