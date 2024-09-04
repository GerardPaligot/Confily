package org.gdglille.devfest.android.theme.m3.events.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.EventItemListUi
import com.paligot.confily.style.theme.Conferences4HallTheme
import org.gdglille.devfest.theme.m3.events.screens.EventList

@Preview
@Composable
private fun EventListPreview() {
    Conferences4HallTheme {
        EventList(events = EventItemListUi.fake, onEventClicked = {})
    }
}
