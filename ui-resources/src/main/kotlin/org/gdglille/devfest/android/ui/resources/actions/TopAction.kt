package org.gdglille.devfest.android.ui.resources.actions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

open class TopAction(
    val id: Int,
    @DrawableRes val icon: Int,
    @StringRes val contentDescription: Int?
)
