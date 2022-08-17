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
import org.gdglille.devfest.android.ui.resources.models.TabActionsUi
import org.gdglille.devfest.repositories.AgendaRepository

private const val EventId = 0
private const val MenusId = 1
private const val QAndAId = 2
private const val CoCId = 3

@ExperimentalPagerApi
@Composable
fun InfoPages(
    tabs: TabActionsUi,
    agendaRepository: AgendaRepository,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onLinkClicked: (url: String?) -> Unit,
    onReportClicked: () -> Unit
) {
    LaunchedEffect(pagerState.currentPage) {
        when (pagerState.currentPage) {
            EventId -> viewModel.updateFabUi(Screen.Event.route)
            MenusId -> viewModel.updateFabUi(Screen.Menus.route)
            QAndAId -> viewModel.updateFabUi(Screen.QAndA.route)
            CoCId -> viewModel.updateFabUi(Screen.CoC.route)
        }
    }
    val count = tabs.tabActions.count()
    HorizontalPager(count = if (count == EventId) MenusId else count, state = pagerState) { page ->
        when (page) {
            EventId -> EventVM(
                agendaRepository = agendaRepository,
                modifier = modifier.fillMaxSize(),
                onLinkClicked = onLinkClicked,
                onItineraryClicked = onItineraryClicked
            )

            MenusId -> MenusVM(
                agendaRepository = agendaRepository,
                modifier = modifier.fillMaxSize()
            )

            QAndAId -> QAndAListVM(
                agendaRepository = agendaRepository,
                modifier = modifier.fillMaxSize(),
                onLinkClicked = onLinkClicked
            )

            CoCId -> CoCVM(
                agendaRepository = agendaRepository,
                modifier = modifier.fillMaxSize(),
                onReportClicked = onReportClicked
            )

            else -> TODO("Screen not implemented")
        }
    }
}
