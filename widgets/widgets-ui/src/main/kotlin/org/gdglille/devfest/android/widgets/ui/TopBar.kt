package org.gdglille.devfest.android.widgets.ui

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.components.TitleBar

@Composable
fun TopBar(
    title: String,
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
    modifier: GlanceModifier = GlanceModifier
) {
    TitleBar(
        title = title,
        startIcon = ImageProvider(iconId),
        actions = {
            CircleIconButton(
                imageProvider = ImageProvider(R.drawable.refresh),
                contentDescription = LocalContext.current.getString(R.string.widget_semantic_refresh),
                onClick = onClick
            )
        },
        modifier = modifier
    )
}
