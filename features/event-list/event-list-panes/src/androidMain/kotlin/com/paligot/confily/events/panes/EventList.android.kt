package com.paligot.confily.events.panes

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.events.ui.models.EventItemListUi
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun EventListPreview() {
    ConfilyTheme {
        EventList(events = EventItemListUi.fake, onEventClicked = {})
    }
}
