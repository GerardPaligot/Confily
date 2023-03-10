package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import org.gdglille.devfest.android.theme.vitamin.features.viewmodels.HomeViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.Screen
import org.gdglille.devfest.android.theme.vitamin.ui.TabActions
import org.gdglille.devfest.android.ui.resources.models.TabActionsUi
import org.gdglille.devfest.repositories.AgendaRepository

@ExperimentalPagerApi
@Composable
fun InfoPages(
    tabs: TabActionsUi,
    agendaRepository: AgendaRepository,
    viewModel: HomeViewModel,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onLinkClicked: (url: String?) -> Unit,
    onReportByPhoneClicked: (String) -> Unit,
    onReportByEmailClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
) {
    LaunchedEffect(pagerState.currentPage) {
        when (tabs.actions[pagerState.currentPage].route) {
            TabActions.event.route -> viewModel.updateFabUi(Screen.Event.route)
            TabActions.menus.route -> viewModel.updateFabUi(Screen.Menus.route)
            TabActions.qanda.route -> viewModel.updateFabUi(Screen.QAndA.route)
            TabActions.coc.route -> viewModel.updateFabUi(Screen.CoC.route)
        }
    }
    val count = tabs.actions.count()
    HorizontalPager(count = if (count == 0) 1 else count, state = pagerState) { page ->
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
