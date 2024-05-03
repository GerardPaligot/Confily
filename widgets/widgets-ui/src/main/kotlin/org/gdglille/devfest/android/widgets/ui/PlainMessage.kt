package org.gdglille.devfest.android.widgets.ui

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import androidx.glance.text.TextDefaults
import androidx.glance.unit.ColorProvider

@Composable
fun Loading(modifier: GlanceModifier = GlanceModifier) {
    PlainMessage(
        text = LocalContext.current.getString(R.string.widget_text_loading),
        modifier = modifier
    )
}

@Composable
fun NoEvent(modifier: GlanceModifier = GlanceModifier) {
    PlainMessage(
        text = LocalContext.current.getString(R.string.widget_text_no_event),
        modifier = modifier
    )
}

@Composable
private fun PlainMessage(
    text: String,
    modifier: GlanceModifier = GlanceModifier,
    color: ColorProvider = GlanceTheme.colors.onSurface
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextDefaults.defaultTextStyle.copy(color = color)
        )
    }
}
