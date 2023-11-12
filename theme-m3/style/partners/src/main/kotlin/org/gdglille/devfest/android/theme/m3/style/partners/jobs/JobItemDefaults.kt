package org.gdglille.devfest.android.theme.m3.style.partners.jobs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import org.gdglille.devfest.android.theme.m3.style.toColor
import org.gdglille.devfest.android.theme.m3.style.toShape
import org.gdglille.devfest.android.theme.m3.style.toTextStyle

object JobItemDefaults {
    val containerColor: Color
        @Composable
        get() = JobItemTokens.ContainerColor.toColor()
    val shape: Shape
        @Composable
        get() = JobItemTokens.ContainerShape.toShape()
    val titleTextStyle: TextStyle
        @Composable
        get() = JobItemTokens.TitleTextStyle.toTextStyle()
    val descriptionTextStyle: TextStyle
        @Composable
        get() = JobItemTokens.DescriptionTextStyle.toTextStyle()
}
