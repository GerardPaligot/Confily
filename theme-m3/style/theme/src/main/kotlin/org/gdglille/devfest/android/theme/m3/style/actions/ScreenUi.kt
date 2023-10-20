package org.gdglille.devfest.android.theme.m3.style.actions

import androidx.annotation.StringRes

data class ScreenUi(
    @StringRes val title: Int,
    val topActionsUi: TopActionsUi = TopActionsUi(),
    val tabActionsUi: TabActionsUi = TabActionsUi(),
    val fabAction: FabAction? = null,
    val bottomActionsUi: BottomActionsUi = BottomActionsUi()
)
