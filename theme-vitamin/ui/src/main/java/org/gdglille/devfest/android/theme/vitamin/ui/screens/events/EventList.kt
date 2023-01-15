package org.gdglille.devfest.android.theme.vitamin.ui.screens.events

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.gdglille.devfest.android.theme.vitamin.ui.R
import org.gdglille.devfest.android.theme.vitamin.ui.TabActions
import org.gdglille.devfest.android.theme.vitamin.ui.components.events.EventItem
import org.gdglille.devfest.android.theme.vitamin.ui.components.structure.VitaminScaffold
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.models.EventItemListUi

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun EventList(
    events: EventItemListUi,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    isLoading: Boolean = false,
    onEventClicked: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val tabActions = remember { listOf(TabActions.futureEvents, TabActions.pastEvents) }
    VitaminScaffold(
        title = R.string.screen_events,
        tabActions = tabActions,
        tabSelectedIndex = pagerState.currentPage,
        onTabClicked = {
            scope.launch { pagerState.animateScrollToPage(tabActions.indexOf(it)) }
        }
    ) { padding ->
        HorizontalPager(count = 2, state = pagerState) { page ->
            val items = if (page == 0) events.future else events.past
            LazyColumn(
                modifier = modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(vertical = 24.dp)
            ) {
                items(items, key = { it.id }) { item ->
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
}

@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
fun EventListPreview() {
    Conferences4HallTheme {
        EventList(events = EventItemListUi.fake, onEventClicked = {})
    }
}
