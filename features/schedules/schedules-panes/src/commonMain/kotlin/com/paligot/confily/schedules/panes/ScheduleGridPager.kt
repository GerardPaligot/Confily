package com.paligot.confily.schedules.panes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paligot.confily.navigation.ActionIds
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_agenda
import com.paligot.confily.resources.text_now_not_today
import com.paligot.confily.schedules.panes.models.AgendaUi
import com.paligot.confily.schedules.ui.models.TalkItemUi
import com.paligot.confily.style.theme.Scaffold
import com.paligot.confily.style.theme.actions.TabActionsUi
import com.paligot.confily.style.theme.actions.TopActionsUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScheduleGridPager(
    agendas: ImmutableList<AgendaUi>,
    isRefreshing: Boolean,
    tabSelected: Int?,
    onTalkClicked: (id: String) -> Unit,
    onEventSessionClicked: (id: String) -> Unit,
    onFilterClicked: () -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    isCurrentDay: Boolean = false,
    currentTimeSlotKey: String? = null,
    topActionsUi: TopActionsUi = TopActionsUi(),
    tabActionsUi: TabActionsUi = TabActionsUi(),
    pagerState: PagerState = rememberPagerState(pageCount = { tabActionsUi.actions.count() }),
    isSmallSize: Boolean = false,
    isLoading: Boolean = false
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val notTodayMessage = stringResource(Resource.string.text_now_not_today)
    // Persist each day's scroll position so it survives the screen leaving
    // composition (e.g. navigating to a session detail and back).
    val gridStates = rememberSaveable(
        agendas.size,
        saver = listSaver<List<LazyGridState>, Int>(
            save = { states ->
                states.flatMap {
                    listOf(it.firstVisibleItemIndex, it.firstVisibleItemScrollOffset)
                }
            },
            restore = { saved ->
                val restored = saved.chunked(2).map { (index, offset) ->
                    LazyGridState(index, offset)
                }
                // Always match the current page count so indexing by page is safe,
                // even if the number of days changed while away from the screen.
                List(agendas.size) { restored.getOrNull(it) ?: LazyGridState() }
            }
        )
    ) {
        List(agendas.size) { LazyGridState() }
    }
    LaunchedEffect(key1 = Unit) {
        if (tabSelected != null) {
            pagerState.scrollToPage(tabSelected)
        }
    }
    Box {
        Scaffold(
            title = stringResource(Resource.string.screen_agenda),
            modifier = modifier,
            topActions = topActionsUi,
            tabActions = tabActionsUi,
            pagerState = pagerState,
            hasScrollBehavior = false,
            onActionClicked = {
                when (it.id) {
                    ActionIds.FILTERS -> {
                        onFilterClicked()
                    }

                    ActionIds.SCROLL_TO_NOW -> {
                        if (isCurrentDay && tabSelected != null) {
                            scope.launch {
                                pagerState.scrollToPage(tabSelected)
                                val index = gridIndexForTimeSlot(
                                    agendas[tabSelected],
                                    currentTimeSlotKey
                                )
                                if (index != null) {
                                    gridStates[tabSelected].animateScrollToItem(index)
                                }
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(notTodayMessage)
                            }
                        }
                    }
                }
            }
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .fillMaxSize(),
                verticalAlignment = Alignment.Top
            ) { page ->
                ScheduleGridContent(
                    agenda = agendas[page],
                    isRefreshing = isRefreshing,
                    onTalkClicked = onTalkClicked,
                    onEventSessionClicked = onEventSessionClicked,
                    onFavoriteClicked = onFavoriteClicked,
                    onRefresh = onRefresh,
                    isSmallSize = isSmallSize,
                    isLoading = isLoading,
                    state = gridStates[page]
                )
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

private fun gridIndexForTimeSlot(agenda: AgendaUi, timeSlotKey: String?): Int? {
    if (timeSlotKey == null) return null
    val sortedKeys = agenda.sessions.keys.sorted()
    var index = 0
    for (key in sortedKeys) {
        if (key == timeSlotKey) return index
        // 1 header item + N session items per slot
        index += 1 + (agenda.sessions[key]?.size ?: 0)
    }
    return null
}
