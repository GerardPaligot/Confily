package org.gdglille.devfest.android.ui.resources

import androidx.annotation.StringRes

data class TabAction(
    val route: String,
    @StringRes val label: Int
)
