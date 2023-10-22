package org.gdglille.devfest.android.theme.m3.style.schedules.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle

object ScheduleCardDefaults {
    val style: TextStyle
        @Composable
        get() = ScheduleCardTextStyleTokens.TitleMedium.toTextStyle()

    val shape: Shape
        @Composable
        get() = ScheduleCardShapeTokens.ContainerShape.toShape()

    @Composable
    fun cardColors(
        titleColor: Color = ScheduleCardColorTokens.OnSurfaceColor.toColor(),
        favoriteColor: Color = ScheduleCardColorTokens.SecondaryColor.toColor(),
        unFavoriteColor: Color = ScheduleCardColorTokens.OnBackgroundColor.toColor()
    ): ScheduleCardColors = remember(titleColor, favoriteColor, unFavoriteColor) {
        ScheduleCardColors(
            titleColor = titleColor,
            favoriteColor = favoriteColor,
            unFavoriteColor = unFavoriteColor
        )
    }
}
