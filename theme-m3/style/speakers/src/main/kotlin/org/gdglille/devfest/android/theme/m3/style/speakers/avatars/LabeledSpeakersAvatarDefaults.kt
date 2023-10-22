package org.gdglille.devfest.android.theme.m3.style.speakers.avatars

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import org.gdglille.devfest.android.theme.m3.style.toColor
import org.gdglille.devfest.android.theme.m3.style.toTextStyle

object LabeledSpeakersAvatarDefaults {
    val smallTextStyle: TextStyle
        @Composable
        get() = LabeledSpeakersAvatarSmallTokens.LabelTextStyle.toTextStyle()

    val contentColor: Color
        @Composable
        get() = LabeledSpeakersAvatarSmallTokens.ContentColor.toColor()
}
