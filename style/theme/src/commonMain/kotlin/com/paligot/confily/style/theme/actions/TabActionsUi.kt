package com.paligot.confily.style.theme.actions

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class TabActionsUi(
    val scrollable: Boolean = false,
    val actions: ImmutableList<TabAction> = persistentListOf()
)
