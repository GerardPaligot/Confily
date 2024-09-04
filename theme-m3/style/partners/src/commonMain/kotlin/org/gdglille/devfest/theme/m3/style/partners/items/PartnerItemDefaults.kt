package org.gdglille.devfest.theme.m3.style.partners.items

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import com.paligot.confily.style.theme.toColor
import com.paligot.confily.style.theme.toShape

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
