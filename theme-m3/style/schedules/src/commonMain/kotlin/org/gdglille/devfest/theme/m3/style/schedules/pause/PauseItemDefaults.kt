package org.gdglille.devfest.theme.m3.style.schedules.pause

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.paligot.confily.style.theme.toColor
import com.paligot.confily.style.theme.toDp
import com.paligot.confily.style.theme.toShape
import com.paligot.confily.style.theme.toTextStyle

object PauseItemDefaults {
    @Composable
    fun smallContainerColor(clickable: Boolean): Color = if (clickable) {
        PauseItemSmallTokens.ClickableContainerColor.toColor()
    } else {
        PauseItemSmallTokens.ContainerColor.toColor()
    }

    val smallTitleTextStyle: TextStyle
        @Composable
        get() = PauseItemSmallTokens.TitleTextStyle.toTextStyle()

    val smallShape: Shape
        @Composable
        get() = PauseItemSmallTokens.ContainerShape.toShape()

    val smallContentPadding: PaddingValues
        @Composable
        get() = PaddingValues(
            horizontal = PauseItemMediumTokens.ContainerPadding.toDp(),
            vertical = PauseItemSmallTokens.ContainerPadding.toDp()
        )

    @Composable
    fun mediumContainerColor(clickable: Boolean): Color = if (clickable) {
        PauseItemMediumTokens.ClickableContainerColor.toColor()
    } else {
        PauseItemMediumTokens.ContainerColor.toColor()
    }

    val mediumTitleTextStyle: TextStyle
        @Composable
        get() = PauseItemMediumTokens.TitleTextStyle.toTextStyle()

    val mediumShape: Shape
        @Composable
        get() = PauseItemMediumTokens.ContainerShape.toShape()
}
