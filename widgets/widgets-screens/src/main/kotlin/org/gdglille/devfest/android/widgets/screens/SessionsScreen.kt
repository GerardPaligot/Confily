package org.gdglille.devfest.android.widgets.screens

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.appwidget.components.Scaffold
import com.paligot.confily.models.ui.EventInfoUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.widgets.ui.Loading
import com.paligot.confily.widgets.ui.NoEvent
import com.paligot.confily.widgets.ui.SessionList
import com.paligot.confily.widgets.ui.TopBar
import kotlinx.collections.immutable.ImmutableList
import org.gdglille.devfest.android.widgets.ui.R

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
