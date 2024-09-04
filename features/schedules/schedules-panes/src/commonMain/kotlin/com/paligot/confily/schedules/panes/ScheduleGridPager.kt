package com.paligot.confily.schedules.panes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.navigation.ActionIds
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_agenda
import com.paligot.confily.style.theme.Scaffold
import com.paligot.confily.style.theme.actions.TabActionsUi
import com.paligot.confily.style.theme.actions.TopActionsUi
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleGridPager(
    agendas: ImmutableList<com.paligot.confily.models.ui.AgendaUi>,
    onTalkClicked: (id: String) -> Unit,
    onEventSessionClicked: (id: String) -> Unit,
    onFilterClicked: () -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    topActionsUi: TopActionsUi = TopActionsUi(),
    tabActionsUi: TabActionsUi = TabActionsUi(),
    pagerState: PagerState = rememberPagerState(pageCount = { tabActionsUi.actions.count() }),
    isSmallSize: Boolean = false,
    isLoading: Boolean = false
) {
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
                onTalkClicked = onTalkClicked,
                onEventSessionClicked = onEventSessionClicked,
                onFavoriteClicked = onFavoriteClicked,
                isSmallSize = isSmallSize,
                isLoading = isLoading,
                state = state
            )
        }
    }
}
