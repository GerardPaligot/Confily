package org.gdglille.devfest.theme.m3.style.markdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.paligot.confily.style.theme.toColor
import com.paligot.confily.style.theme.toTextStyle

object MarkdownTextDefaults {
    val bodyColor: Color
        @Composable
        get() = MarkdownTextTokens.BodyColor.toColor()

    val bodyTextStyle: TextStyle
        @Composable
        get() = MarkdownTextTokens.BodyTextStyle.toTextStyle()
}
