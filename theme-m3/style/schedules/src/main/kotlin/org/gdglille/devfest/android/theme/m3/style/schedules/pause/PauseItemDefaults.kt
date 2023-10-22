package org.gdglille.devfest.android.theme.m3.style.schedules.pause

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import org.gdglille.devfest.android.theme.m3.style.toColor
import org.gdglille.devfest.android.theme.m3.style.toShape
import org.gdglille.devfest.android.theme.m3.style.toTextStyle

object PauseItemDefaults {
    val containerColor: Color
        @Composable
        get() = PauseItemTokens.ContainerColor.toColor()

    val titleTextStyle: TextStyle
        @Composable
        get() = PauseItemTokens.TitleTextStyle.toTextStyle()

    val shape: Shape
        @Composable
        get() = PauseItemTokens.ContainerShape.toShape()
}
