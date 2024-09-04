package com.paligot.confily.style.theme.actions

import org.jetbrains.compose.resources.StringResource

data class TabAction(
    val route: String,
    val labelId: StringResource,
    val label: String? = null
)
