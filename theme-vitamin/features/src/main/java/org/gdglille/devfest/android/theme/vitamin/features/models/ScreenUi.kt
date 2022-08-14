package org.gdglille.devfest.android.theme.vitamin.features.models

import androidx.annotation.StringRes
import org.gdglille.devfest.android.ui.resources.BottomAction
import org.gdglille.devfest.android.ui.resources.FabAction
import org.gdglille.devfest.android.ui.resources.TabAction
import org.gdglille.devfest.android.ui.resources.TopAction

data class ScreenUi(
    @StringRes val title: Int,
    val topActions: List<TopAction> = emptyList(),
    val tabActions: List<TabAction> = emptyList(),
    val bottomActions: List<BottomAction> = emptyList(),
    val fabAction: FabAction? = null,
    val scrollable: Boolean = false
)
