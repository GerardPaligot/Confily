package org.gdglille.devfest.android.theme.m3.style.actions

import androidx.annotation.StringRes

data class TabAction(
    val route: String,
    @StringRes val labelId: Int,
    val label: String? = null
)
