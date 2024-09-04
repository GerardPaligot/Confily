package com.paligot.confily.style.partners.jobs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.paligot.confily.style.theme.toColor
import com.paligot.confily.style.theme.toShape
import com.paligot.confily.style.theme.toTextStyle

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
