package org.gdglille.devfest.android.theme.m3.events.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.EventItemUi
import com.paligot.confily.style.theme.Conferences4HallTheme
import org.gdglille.devfest.theme.m3.events.ui.EventItem

@Preview
@Composable
private fun EventItemPreview() {
    Conferences4HallTheme {
        EventItem(
            item = EventItemUi.fake,
            onClick = {}
        )
    }
}
