package org.gdglille.devfest.android.theme.m3.style.schedules.filters

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import org.gdglille.devfest.android.theme.m3.style.toTextStyle

object FilterSectionDefaults {
    val style: TextStyle
        @Composable
        get() = FilterSectionTokens.TitleSectionTextStyle.toTextStyle()
}
