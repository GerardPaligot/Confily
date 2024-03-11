package org.gdglille.devfest.android.theme.m3.style.actions

import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

@OptIn(ExperimentalResourceApi::class)
data class TabAction(
    val route: String,
    val labelId: StringResource,
    val label: String? = null
)
