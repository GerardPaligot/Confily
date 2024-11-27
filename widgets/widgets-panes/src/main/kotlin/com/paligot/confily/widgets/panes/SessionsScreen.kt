package com.paligot.confily.widgets.panes

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.appwidget.components.Scaffold
import com.paligot.confily.widgets.ui.Loading
import com.paligot.confily.widgets.ui.NoEvent
import com.paligot.confily.widgets.ui.R
import com.paligot.confily.widgets.ui.SessionList
import com.paligot.confily.widgets.ui.TopBar
import com.paligot.confily.widgets.ui.models.SessionItemUi
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SessionsScreen(
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
    eventName: String?,
    sessions: ImmutableList<SessionItemUi>,
    onItemClick: (String) -> Action,
    modifier: GlanceModifier = GlanceModifier
) {
    Scaffold(
        titleBar = {
            TopBar(
                title = eventName
                    ?: LocalContext.current.getString(R.string.widget_title_no_event),
                iconId = iconId,
                onClick = onClick
            )
        },
        content = {
            if (eventName == null) {
                NoEvent()
            } else if (sessions.isEmpty()) {
                Loading()
            } else {
                SessionList(sessions = sessions, onItemClick = onItemClick)
            }
        },
        modifier = modifier
    )
}
