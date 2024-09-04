package com.paligot.confily.style.speakers.avatar

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import com.paligot.confily.style.theme.toColor
import com.paligot.confily.style.theme.toShape

object SpeakerAvatarDefaults {
    val placeholder: Painter
        @Composable
        get() = ColorPainter(
            color = SpeakerAvatarTokens.ContainerColor.toColor()
        )

    val mediumShape: Shape
        @Composable
        get() = SpeakerAvatarMediumTokens.ContainerShape.toShape()

    val largeShape: Shape
        @Composable
        get() = SpeakerAvatarLargeTokens.ContainerShape.toShape()

    val borderedSmallShape: Shape
        @Composable
        get() = BorderedSpeakerAvatarSmallTokens.ContainerShape.toShape()

    val borderedMediumShape: Shape
        @Composable
        get() = BorderedSpeakerAvatarMediumTokens.ContainerShape.toShape()

    val borderedSmall: BorderStroke
        @Composable
        get() = BorderStroke(
            width = BorderedSpeakerAvatarSmallTokens.BorderWidth,
            color = contentColorFor(SpeakerAvatarTokens.ContainerColor.toColor())
        )

    val borderedMedium: BorderStroke
        @Composable
        get() = BorderStroke(
            width = BorderedSpeakerAvatarMediumTokens.BorderWidth,
            color = contentColorFor(SpeakerAvatarTokens.ContainerColor.toColor())
        )
}
