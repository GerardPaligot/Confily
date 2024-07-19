package org.gdglille.devfest.theme.m3.style.speakers.items

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import org.gdglille.devfest.android.theme.m3.style.toColor
import org.gdglille.devfest.android.theme.m3.style.toShape
import org.gdglille.devfest.android.theme.m3.style.toTextStyle

object SpeakerItemDefaults {
    val containerColor: Color
        @Composable
        get() = SpeakerItemLargeTokens.ContainerColor.toColor()

    val containerShape: Shape
        @Composable
        get() = SpeakerItemLargeTokens.ContainerShape.toShape()

    val nameTextStyle: TextStyle
        @Composable
        get() = SpeakerItemLargeTokens.NameTextStyle.toTextStyle()

    val descriptionTextStyle: TextStyle
        @Composable
        get() = SpeakerItemLargeTokens.DescriptionTextStyle.toTextStyle()
}
