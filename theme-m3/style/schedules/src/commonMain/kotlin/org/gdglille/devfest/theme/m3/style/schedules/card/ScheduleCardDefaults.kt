package org.gdglille.devfest.theme.m3.style.schedules.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import org.gdglille.devfest.android.theme.m3.style.toColor
import org.gdglille.devfest.android.theme.m3.style.toShape
import org.gdglille.devfest.android.theme.m3.style.toTextStyle

object ScheduleCardDefaults {
    val smallTextStyle: TextStyle
        @Composable
        get() = ScheduleCardSmallTokens.TitleTextStyle.toTextStyle()

    val smallShape: Shape
        @Composable
        get() = ScheduleCardSmallTokens.ContainerShape.toShape()

    val mediumTextStyle: TextStyle
        @Composable
        get() = ScheduleCardMediumTokens.TitleTextStyle.toTextStyle()

    val mediumShape: Shape
        @Composable
        get() = ScheduleCardMediumTokens.ContainerShape.toShape()

    @Composable
    fun cardColors(
        titleColor: Color = ScheduleCardMediumTokens.TitleColor.toColor(),
        favoriteColor: Color = ScheduleCardMediumTokens.IconFavoriteColor.toColor(),
        unFavoriteColor: Color = ScheduleCardMediumTokens.IconUnFavoriteColor.toColor()
    ): ScheduleCardColors = remember(titleColor, favoriteColor, unFavoriteColor) {
        ScheduleCardColors(
            titleColor = titleColor,
            favoriteColor = favoriteColor,
            unFavoriteColor = unFavoriteColor
        )
    }
}
