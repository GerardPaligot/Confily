package org.gdglille.devfest.android.widgets.screens

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.appwidget.components.Scaffold
import kotlinx.collections.immutable.ImmutableList
import org.gdglille.devfest.android.widgets.ui.Loading
import org.gdglille.devfest.android.widgets.ui.NoEvent
import org.gdglille.devfest.android.widgets.ui.R
import org.gdglille.devfest.android.widgets.ui.SessionList
import org.gdglille.devfest.android.widgets.ui.TopBar
import org.gdglille.devfest.models.ui.EventInfoUi
import org.gdglille.devfest.models.ui.TalkItemUi

@Composable
fun SessionsScreen(
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
    eventInfoUi: EventInfoUi?,
    talks: ImmutableList<TalkItemUi>,
    onItemClick: (String) -> Action,
    modifier: GlanceModifier = GlanceModifier
) {
    Scaffold(
        titleBar = {
            TopBar(
                title = eventInfoUi?.name
                    ?: LocalContext.current.getString(R.string.widget_title_no_event),
                iconId = iconId,
                onClick = onClick
            )
        },
        content = {
            if (eventInfoUi == null) {
                NoEvent()
            } else if (talks.isEmpty()) {
                Loading()
            } else {
                SessionList(talks = talks, onItemClick = onItemClick)
            }
        },
        modifier = modifier
    )
}
