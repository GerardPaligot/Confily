package com.paligot.confily.style.speakers.items

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.paligot.confily.style.theme.toColor
import com.paligot.confily.style.theme.toShape
import com.paligot.confily.style.theme.toTextStyle

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
