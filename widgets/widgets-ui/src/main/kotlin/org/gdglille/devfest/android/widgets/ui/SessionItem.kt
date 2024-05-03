package org.gdglille.devfest.android.widgets.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextDefaults
import androidx.glance.unit.ColorProvider

@Composable
fun SessionItem(
    title: String,
    time: String,
    room: String,
    duration: String,
    onClick: Action,
    modifier: GlanceModifier = GlanceModifier,
    containerColor: ColorProvider = GlanceTheme.colors.primaryContainer,
    contentColor: ColorProvider = GlanceTheme.colors.onPrimaryContainer
) {
    Column(
        modifier = modifier
            .background(containerColor)
            .cornerRadius(8.dp)
            .padding(8.dp)
            .clickable(onClick)
    ) {
        Text(
            text = title,
            style = TextDefaults.defaultTextStyle.copy(color = contentColor),
            modifier = GlanceModifier.padding(bottom = 4.dp)
        )
        Row {
            Tag(
                resId = R.drawable.today,
                text = time
            )
            Tag(
                resId = R.drawable.videocam,
                text = room,
                modifier = GlanceModifier.padding(start = 16.dp)
            )
            Tag(
                resId = R.drawable.schedule,
                text = duration,
                modifier = GlanceModifier.padding(start = 16.dp)
            )
        }
    }
}
