package com.paligot.confily.events.panes

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.EventItemListUi
import com.paligot.confily.style.theme.Conferences4HallTheme

@Preview
@Composable
private fun EventListPreview() {
    Conferences4HallTheme {
        EventList(events = EventItemListUi.fake, onEventClicked = {})
    }
}
