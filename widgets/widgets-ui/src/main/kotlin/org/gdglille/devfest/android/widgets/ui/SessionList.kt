package org.gdglille.devfest.android.widgets.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import com.paligot.confily.core.extensions.formatHoursMinutes
import com.paligot.confily.models.ui.TalkItemUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDateTime

@Composable
fun SessionList(
    talks: ImmutableList<TalkItemUi>,
    onItemClick: (String) -> Action,
    modifier: GlanceModifier = GlanceModifier
) {
    LazyColumn(modifier = modifier) {
        items(talks) {
            Box(modifier = GlanceModifier.padding(8.dp)) {
                SessionItem(
                    title = it.title,
                    time = LocalDateTime.parse(it.startTime).formatHoursMinutes(),
                    room = it.room,
                    duration = it.time,
                    modifier = GlanceModifier.fillMaxWidth(),
                    onClick = onItemClick(it.id)
                )
            }
        }
    }
}
