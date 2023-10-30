package org.gdglille.devfest.android.theme.m3.style.socials

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import org.gdglille.devfest.android.theme.m3.style.toColor
import org.gdglille.devfest.android.theme.m3.style.toTextStyle

object SocialsSectionDefaults {
    val titleColor: Color
        @Composable
        get() = SocialsSectionTokens.TitleColor.toColor()

    val titleTextStyle: TextStyle
        @Composable
        get() = SocialsSectionTokens.TitleTextStyle.toTextStyle()

    val pronounsColor: Color
        @Composable
        get() = SocialsSectionTokens.PronounsColor.toColor().copy(alpha = .5f)

    val pronounsTextStyle: TextStyle
        @Composable
        get() = SocialsSectionTokens.PronounsTextStyle.toTextStyle()

    val subTitleColor: Color
        @Composable
        get() = SocialsSectionTokens.SubTitleColor.toColor()

    val subTitleTextStyle: TextStyle
        @Composable
        get() = SocialsSectionTokens.SubTitleTextStyle.toTextStyle()

    val bodyColor: Color
        @Composable
        get() = SocialsSectionTokens.BodyColor.toColor()

    val bodyTextStyle: TextStyle
        @Composable
        get() = SocialsSectionTokens.BodyTextStyle.toTextStyle()
}
