package org.gdglille.devfest.android.theme.m3.style.partners

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import org.gdglille.devfest.android.theme.m3.style.toColor
import org.gdglille.devfest.android.theme.m3.style.toShape

object PartnerItemDefaults {
    val containerColor: Color
        @Composable
        get() = PartnerItemTokens.ContainerColor.toColor()
    val placeholderPainter: Painter
        @Composable
        get() = ColorPainter(PartnerItemTokens.PlaceHolderColor.toColor())
    val shape: Shape
        @Composable
        get() = PartnerItemTokens.ContainerShape.toShape()
}
