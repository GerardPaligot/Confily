package org.gdglille.devfest.android.theme.m3.style.schedules.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import org.gdglille.devfest.android.theme.m3.style.toColor
import org.gdglille.devfest.android.theme.m3.style.toShape
import org.gdglille.devfest.android.theme.m3.style.toTextStyle

object ScheduleCardDefaults {
    val style: TextStyle
        @Composable
        get() = ScheduleCardTokens.TitleTextStyle.toTextStyle()

    val shape: Shape
        @Composable
        get() = ScheduleCardTokens.ContainerShape.toShape()

    @Composable
    fun cardColors(
        titleColor: Color = ScheduleCardTokens.TitleColor.toColor(),
        favoriteColor: Color = ScheduleCardTokens.IconFavoriteColor.toColor(),
        unFavoriteColor: Color = ScheduleCardTokens.IconUnFavoriteColor.toColor()
    ): ScheduleCardColors = remember(titleColor, favoriteColor, unFavoriteColor) {
        ScheduleCardColors(
            titleColor = titleColor,
            favoriteColor = favoriteColor,
            unFavoriteColor = unFavoriteColor
        )
    }
}
