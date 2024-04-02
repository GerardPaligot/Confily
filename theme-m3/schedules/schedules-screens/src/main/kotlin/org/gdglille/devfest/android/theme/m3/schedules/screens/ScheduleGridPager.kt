package org.gdglille.devfest.android.theme.m3.schedules.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import org.gdglille.devfest.android.theme.m3.navigation.ActionIds
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.theme.m3.style.Scaffold
import org.gdglille.devfest.android.theme.m3.style.actions.TabActionsUi
import org.gdglille.devfest.android.theme.m3.style.actions.TopActionsUi
import org.gdglille.devfest.android.shared.resources.screen_agenda
import org.gdglille.devfest.models.ui.AgendaUi
import org.gdglille.devfest.models.ui.TalkItemUi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
fun ScheduleGridPager(
    agendas: ImmutableList<AgendaUi>,
    onTalkClicked: (id: String) -> Unit,
    onFilterClicked: () -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    modifier: Modifier = Modifier,
    topActionsUi: TopActionsUi = TopActionsUi(),
    tabActionsUi: TabActionsUi = TabActionsUi(),
    pagerState: PagerState = rememberPagerState(pageCount = { tabActionsUi.actions.count() }),
    isSmallSize: Boolean = false,
    isLoading: Boolean = false,
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
                onFavoriteClicked = onFavoriteClicked,
                isSmallSize = isSmallSize,
                isLoading = isLoading
            )
        }
    }
}
