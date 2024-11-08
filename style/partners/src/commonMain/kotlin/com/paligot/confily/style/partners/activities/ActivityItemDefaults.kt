package com.paligot.confily.style.partners.activities

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.paligot.confily.style.theme.toColor
import com.paligot.confily.style.theme.toDp
import com.paligot.confily.style.theme.toShape
import com.paligot.confily.style.theme.toTextStyle

object ActivityItemDefaults {
    val smallContainerColor: Color
        @Composable
        get() = ActivityItemSmallTokens.ContainerColor.toColor()

    val smallTitleTextStyle: TextStyle
        @Composable
        get() = ActivityItemSmallTokens.TitleTextStyle.toTextStyle()

    val smallShape: Shape
        @Composable
        get() = ActivityItemSmallTokens.ContainerShape.toShape()

    val smallContentPadding: PaddingValues
        @Composable
        get() = PaddingValues(
            horizontal = ActivityItemMediumTokens.ContainerPadding.toDp(),
            vertical = ActivityItemSmallTokens.ContainerPadding.toDp()
        )

    val mediumContainerColor: Color
        @Composable
        get() = ActivityItemMediumTokens.ContainerColor.toColor()

    val mediumTitleTextStyle: TextStyle
        @Composable
        get() = ActivityItemMediumTokens.TitleTextStyle.toTextStyle()

    val mediumShape: Shape
        @Composable
        get() = ActivityItemMediumTokens.ContainerShape.toShape()

    val mediumContentPadding: PaddingValues
        @Composable
        get() = PaddingValues(ActivityItemMediumTokens.ContainerPadding.toDp())
}
