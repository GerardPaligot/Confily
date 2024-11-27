package com.paligot.confily.widgets.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import com.paligot.confily.widgets.ui.models.SessionItemUi
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SessionList(
    sessions: ImmutableList<SessionItemUi>,
    onItemClick: (String) -> Action,
    modifier: GlanceModifier = GlanceModifier
) {
    LazyColumn(modifier = modifier) {
        items(sessions) {
            Box(modifier = GlanceModifier.padding(8.dp)) {
                SessionItem(
                    title = it.title,
                    time = it.slotTime,
                    room = it.room,
                    duration = it.duration,
                    modifier = GlanceModifier.fillMaxWidth(),
                    onClick = onItemClick(it.id)
                )
            }
        }
    }
}
