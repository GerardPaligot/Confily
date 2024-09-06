package com.paligot.confily.widgets.ui

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import androidx.glance.text.TextDefaults
import androidx.glance.unit.ColorProvider

@Composable
internal fun Tag(
    @DrawableRes resId: Int,
    text: String,
    modifier: GlanceModifier = GlanceModifier,
    containerColor: ColorProvider = GlanceTheme.colors.secondaryContainer,
    contentColor: ColorProvider = GlanceTheme.colors.onSecondaryContainer
) {
    Row(
        modifier = modifier.background(containerColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            provider = ImageProvider(resId),
            contentDescription = null,
            colorFilter = ColorFilter.tint(contentColor),
            modifier = GlanceModifier.size(16.dp)
        )
        Text(
            text = text,
            modifier = GlanceModifier.padding(start = 4.dp),
            style = TextDefaults.defaultTextStyle.copy(color = contentColor)
        )
    }
}
