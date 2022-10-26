package org.gdglille.devfest.android.ui.resources.actions

import androidx.annotation.StringRes

data class TabAction(
    val route: String,
    @StringRes val labelId: Int,
    val label: String? = null
)
