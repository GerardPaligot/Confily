package com.paligot.confily.style.theme.tags

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class TagColors internal constructor(
    val containerColor: Color,
    val contentColor: Color
)
