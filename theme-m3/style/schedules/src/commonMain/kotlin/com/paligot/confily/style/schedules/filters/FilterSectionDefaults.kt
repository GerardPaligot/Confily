package com.paligot.confily.style.schedules.filters

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.paligot.confily.style.theme.toTextStyle

object FilterSectionDefaults {
    val style: TextStyle
        @Composable
        get() = FilterSectionTokens.TitleSectionTextStyle.toTextStyle()
}
