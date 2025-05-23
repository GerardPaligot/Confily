package com.paligot.confily.style.events.socials

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.paligot.confily.style.theme.toColor
import com.paligot.confily.style.theme.toTextStyle

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
}
