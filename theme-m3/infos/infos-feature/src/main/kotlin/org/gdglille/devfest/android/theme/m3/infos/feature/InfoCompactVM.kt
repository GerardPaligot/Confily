package org.gdglille.devfest.android.theme.m3.infos.feature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.theme.m3.navigation.ActionIds
import org.gdglille.devfest.android.theme.m3.navigation.TabActions
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.appbars.TopAppBarContentLayout
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.EventRepository

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfoCompactVM(
    agendaRepository: AgendaRepository,
    eventRepository: EventRepository,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onLinkClicked: (url: String?) -> Unit,
    onDisconnectedClicked: () -> Unit,
    onReportByPhoneClicked: (String) -> Unit,
    onReportByEmailClicked: (String) -> Unit,
    onInnerScreenOpened: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InfoViewModel = viewModel(
        factory = InfoViewModel.Factory.create(agendaRepository, eventRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    val title = stringResource(id = R.string.screen_info)
    when (uiState.value) {
        is InfoUiState.Loading -> TopAppBarContentLayout(title = title, modifier = modifier) {
            EventVM(
                agendaRepository = agendaRepository,
                modifier = Modifier.fillMaxSize(),
                onLinkClicked = onLinkClicked,
                onItineraryClicked = onItineraryClicked
            )
        }

        is InfoUiState.Success -> {
            val uiModel = uiState.value as InfoUiState.Success
            val pagerState =
                rememberPagerState(pageCount = { uiModel.tabActionsUi.actions.count() })
            LaunchedEffect(pagerState.currentPage) {
                onInnerScreenOpened(uiModel.tabActionsUi.actions[pagerState.currentPage].route)
            }
            TopAppBarContentLayout(
                title = title,
                topActions = uiModel.topActionsUi,
                tabActions = uiModel.tabActionsUi,
                onActionClicked = {
                    when (it.id) {
                        ActionIds.DISCONNECT -> {
                            viewModel.disconnect()
                            onDisconnectedClicked()
                        }
                    }
                },
                pagerState = pagerState,
                modifier = modifier
            ) {
                HorizontalPager(state = pagerState) { page ->
                    when (uiModel.tabActionsUi.actions[page].route) {
                        TabActions.event.route -> EventVM(
                            agendaRepository = agendaRepository,
                            modifier = Modifier.fillMaxSize(),
                            onLinkClicked = onLinkClicked,
                            onItineraryClicked = onItineraryClicked
                        )

                        TabActions.menus.route -> MenusVM(
                            agendaRepository = agendaRepository,
                            modifier = Modifier.fillMaxSize()
                        )

                        TabActions.qanda.route -> QAndAListVM(
                            agendaRepository = agendaRepository,
                            modifier = Modifier.fillMaxSize(),
                            onLinkClicked = onLinkClicked
                        )

                        TabActions.coc.route -> CoCVM(
                            agendaRepository = agendaRepository,
                            modifier = Modifier.fillMaxSize(),
                            onReportByPhoneClicked = onReportByPhoneClicked,
                            onReportByEmailClicked = onReportByEmailClicked
                        )

                        else -> TODO("Screen not implemented")
                    }
                }
            }
        }

        is InfoUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
    }
}
