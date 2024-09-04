package org.gdglille.devfest.theme.m3.style.speakers.avatars

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.paligot.confily.style.theme.toColor
import com.paligot.confily.style.theme.toTextStyle

object LabeledSpeakersAvatarDefaults {
    val smallTextStyle: TextStyle
        @Composable
        get() = LabeledSpeakersAvatarSmallTokens.LabelTextStyle.toTextStyle()

    val contentColor: Color
        @Composable
        get() = LabeledSpeakersAvatarSmallTokens.ContentColor.toColor()
}
