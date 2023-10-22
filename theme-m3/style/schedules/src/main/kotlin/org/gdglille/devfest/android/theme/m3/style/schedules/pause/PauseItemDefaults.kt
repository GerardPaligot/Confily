package org.gdglille.devfest.android.theme.m3.style.schedules.pause

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import org.gdglille.devfest.android.theme.m3.style.ColorSchemeTokens
import org.gdglille.devfest.android.theme.m3.style.toColor

object PauseItemDefaults {
    val containerColor: Color
        @Composable
        get() = ColorSchemeTokens.SurfaceColor.toColor()

    val titleTextStyle: TextStyle
        @Composable
        get() = PauseItemTextStyleTokens.TitleMedium.toTextStyle()

    val shape: Shape
        @Composable
        get() = PauseItemShapeTokens.ContainerShape.toShape()
}
