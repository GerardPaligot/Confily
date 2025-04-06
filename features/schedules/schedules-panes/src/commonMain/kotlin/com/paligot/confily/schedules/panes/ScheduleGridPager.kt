package com.paligot.confily.schedules.panes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paligot.confily.navigation.ActionIds
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_agenda
import com.paligot.confily.schedules.panes.models.AgendaUi
import com.paligot.confily.schedules.ui.models.TalkItemUi
import com.paligot.confily.style.theme.Scaffold
import com.paligot.confily.style.theme.actions.TabActionsUi
import com.paligot.confily.style.theme.actions.TopActionsUi
import kotlinx.collections.immutable.ImmutableList
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
    state: LazyGridState = rememberLazyGridState(),
    topActionsUi: TopActionsUi = TopActionsUi(),
    tabActionsUi: TabActionsUi = TabActionsUi(),
    pagerState: PagerState = rememberPagerState(pageCount = { tabActionsUi.actions.count() }),
    isSmallSize: Boolean = false,
    isLoading: Boolean = false
) {
    LaunchedEffect(key1 = Unit) {
        if (tabSelected != null) {
            pagerState.scrollToPage(tabSelected)
        }
    }
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
            ScheduleGridScreen(
                agenda = agendas[page],
                isRefreshing = isRefreshing,
                onTalkClicked = onTalkClicked,
                onEventSessionClicked = onEventSessionClicked,
                onFavoriteClicked = onFavoriteClicked,
                onRefresh = onRefresh,
                isSmallSize = isSmallSize,
                isLoading = isLoading,
                state = state
            )
        }
    }
}
