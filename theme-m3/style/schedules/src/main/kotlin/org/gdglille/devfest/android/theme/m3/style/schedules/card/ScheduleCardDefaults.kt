package org.gdglille.devfest.android.theme.m3.style.schedules.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import org.gdglille.devfest.android.theme.m3.style.ColorSchemeTokens
import org.gdglille.devfest.android.theme.m3.style.TextStyleTokens
import org.gdglille.devfest.android.theme.m3.style.toColor
import org.gdglille.devfest.android.theme.m3.style.toTextStyle

object ScheduleCardDefaults {
    val style: TextStyle
        @Composable
        get() = TextStyleTokens.TitleMedium.toTextStyle()

    val shape: Shape
        @Composable
        get() = ScheduleCardShapeTokens.ContainerShape.toShape()

    @Composable
    fun cardColors(
        titleColor: Color = ColorSchemeTokens.OnSurfaceColor.toColor(),
        favoriteColor: Color = ColorSchemeTokens.SecondaryColor.toColor(),
        unFavoriteColor: Color = ColorSchemeTokens.OnBackgroundColor.toColor()
    ): ScheduleCardColors = remember(titleColor, favoriteColor, unFavoriteColor) {
        ScheduleCardColors(
            titleColor = titleColor,
            favoriteColor = favoriteColor,
            unFavoriteColor = unFavoriteColor
        )
    }
}
