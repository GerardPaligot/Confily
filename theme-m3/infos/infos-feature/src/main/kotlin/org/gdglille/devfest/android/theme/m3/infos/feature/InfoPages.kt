package org.gdglille.devfest.android.theme.m3.infos.feature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import org.gdglille.devfest.android.theme.m3.navigation.TabActions
import org.gdglille.devfest.android.theme.m3.style.actions.TabActionsUi
import org.gdglille.devfest.repositories.AgendaRepository

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfoPages(
    tabs: TabActionsUi,
    agendaRepository: AgendaRepository,
    pagerState: PagerState,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onLinkClicked: (url: String?) -> Unit,
    onReportByPhoneClicked: (String) -> Unit,
    onReportByEmailClicked: (String) -> Unit,
    onEventScreenOpened: () -> Unit,
    onMenusScreenOpened: () -> Unit,
    onQAndAScreenOpened: () -> Unit,
    onCoCScreenOpened: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(pagerState.currentPage) {
        when (tabs.actions[pagerState.currentPage].route) {
            TabActions.event.route -> onEventScreenOpened()
            TabActions.menus.route -> onMenusScreenOpened()
            TabActions.qanda.route -> onQAndAScreenOpened()
            TabActions.coc.route -> onCoCScreenOpened()
        }
    }
    HorizontalPager(state = pagerState) { page ->
        when (tabs.actions[page].route) {
            TabActions.event.route -> EventVM(
                agendaRepository = agendaRepository,
                modifier = modifier.fillMaxSize(),
                onLinkClicked = onLinkClicked,
                onItineraryClicked = onItineraryClicked
            )

            TabActions.menus.route -> MenusVM(
                agendaRepository = agendaRepository,
                modifier = modifier.fillMaxSize()
            )

            TabActions.qanda.route -> QAndAListVM(
                agendaRepository = agendaRepository,
                modifier = modifier.fillMaxSize(),
                onLinkClicked = onLinkClicked
            )

            TabActions.coc.route -> CoCVM(
                agendaRepository = agendaRepository,
                modifier = modifier.fillMaxSize(),
                onReportByPhoneClicked = onReportByPhoneClicked,
                onReportByEmailClicked = onReportByEmailClicked
            )

            else -> TODO("Screen not implemented")
        }
    }
}
