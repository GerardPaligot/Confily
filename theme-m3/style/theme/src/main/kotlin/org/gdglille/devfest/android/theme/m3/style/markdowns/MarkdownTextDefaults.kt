package org.gdglille.devfest.android.theme.m3.style.markdowns

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import org.gdglille.devfest.android.theme.m3.style.socials.SocialsSectionTokens
import org.gdglille.devfest.android.theme.m3.style.toColor
import org.gdglille.devfest.android.theme.m3.style.toTextStyle

object MarkdownTextDefaults {
    val bodyColor: Color
        @Composable
        get() = SocialsSectionTokens.BodyColor.toColor()

    val bodyTextStyle: TextStyle
        @Composable
        get() = SocialsSectionTokens.BodyTextStyle.toTextStyle()
}
