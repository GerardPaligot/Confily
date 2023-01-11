package org.gdglille.devfest.android.screens.events

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.events.EventItem
import org.gdglille.devfest.android.components.structure.Scaffold
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.ui.R
import org.gdglille.devfest.models.EventItemListUi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventList(
    events: EventItemListUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onEventClicked: (String) -> Unit
) {
    Scaffold(title = R.string.screen_events) { padding ->
        LazyColumn(
            modifier = modifier.padding(padding),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            items(events.future, key = { it.id }) { item ->
                EventItem(
                    item = item,
                    isLoading = isLoading,
                    onClick = onEventClicked,
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
    }
}

@Preview
@Composable
fun EventListPreview() {
    Conferences4HallTheme {
        EventList(events = EventItemListUi.fake, onEventClicked = {})
    }
}
