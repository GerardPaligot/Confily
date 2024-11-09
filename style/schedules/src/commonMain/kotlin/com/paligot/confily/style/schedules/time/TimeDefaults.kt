package com.paligot.confily.style.schedules.time

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.paligot.confily.style.theme.toColor
import com.paligot.confily.style.theme.toTextStyle

object TimeDefaults {
    val color: Color
        @Composable
        get() = TimeTokens.ContentColor.toColor()

    val style: TextStyle
        @Composable
        get() = TimeTokens.TimeTextStyle.toTextStyle()
}
