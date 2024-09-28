package com.paligot.confily.events.panes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.confily.events.ui.EventItem
import com.paligot.confily.models.ui.EventItemListUi
import com.paligot.confily.navigation.TabActions
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_events
import com.paligot.confily.style.theme.Scaffold
import com.paligot.confily.style.theme.actions.TabActionsUi
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventList(
    events: EventItemListUi,
    onEventClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    val tabActions = remember {
        TabActionsUi(
            actions = persistentListOf(
                TabActions.futureEvents,
                TabActions.pastEvents
            )
        )
    }
    val pagerState = rememberPagerState(pageCount = { tabActions.actions.count() })
    Scaffold(
        title = stringResource(Resource.string.screen_events),
        tabActions = tabActions,
        pagerState = pagerState
    ) { padding ->
        HorizontalPager(state = pagerState) { page ->
            val items = if (page == 0) events.future else events.past
            LazyColumn(
                modifier = modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = 24.dp)
            ) {
                items(items, key = { it.id }) { item ->
                    EventItem(
                        item = item,
                        isLoading = isLoading,
                        onClick = onEventClicked,
                        modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null)
                    )
                }
            }
        }
    }
}
