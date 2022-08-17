package org.gdglille.devfest.android.ui.resources.actions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

open class BottomAction(
    val route: String,
    @DrawableRes val icon: Int,
    @DrawableRes val iconSelected: Int,
    @StringRes val label: Int,
    @StringRes val contentDescription: Int?,
    val selectedRoutes: List<String> = emptyList()
)
