package org.gdglille.devfest.android.theme.m3.infos.feature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.theme.m3.navigation.ActionIds
import org.gdglille.devfest.android.theme.m3.navigation.TabActions
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.Scaffold
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfoCompactVM(
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onLinkClicked: (url: String?) -> Unit,
    onTicketScannerClicked: () -> Unit,
    onDisconnectedClicked: () -> Unit,
    onReportByPhoneClicked: (String) -> Unit,
    onReportByEmailClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InfoViewModel = koinViewModel()
) {
    val title = stringResource(id = R.string.screen_info)
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is InfoUiState.Loading -> Scaffold(title = title, modifier = modifier) {
            EventVM(
                onLinkClicked = onLinkClicked,
                onItineraryClicked = onItineraryClicked,
                modifier = Modifier.fillMaxSize()
            )
        }

        is InfoUiState.Success -> {
            val pagerState = rememberPagerState(
                pageCount = { uiState.tabActionsUi.actions.count() }
            )
            LaunchedEffect(pagerState.currentPage) {
                viewModel.innerScreenConfig(uiState.tabActionsUi.actions[pagerState.currentPage].route)
            }
            Scaffold(
                title = title,
                modifier = modifier,
                topActions = uiState.topActionsUi,
                tabActions = uiState.tabActionsUi,
                fabAction = uiState.fabAction,
                onActionClicked = {
                    when (it.id) {
                        ActionIds.DISCONNECT -> {
                            viewModel.disconnect()
                            onDisconnectedClicked()
                        }
                    }
                },
                onFabActionClicked = {
                    when (it.id) {
                        ActionIds.SCAN_TICKET -> {
                            onTicketScannerClicked()
                        }

                        else -> TODO("Fab not implemented")
                    }
                },
                pagerState = pagerState
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.padding(it)
                ) { page ->
                    when (uiState.tabActionsUi.actions[page].route) {
                        TabActions.event.route -> EventVM(
                            onLinkClicked = onLinkClicked,
                            onItineraryClicked = onItineraryClicked,
                            modifier = Modifier.fillMaxSize()
                        )

                        TabActions.menus.route -> MenusVM(
                            modifier = Modifier.fillMaxSize()
                        )

                        TabActions.qanda.route -> QAndAListVM(
                            onLinkClicked = onLinkClicked,
                            modifier = Modifier.fillMaxSize()
                        )

                        TabActions.coc.route -> CoCVM(
                            onReportByPhoneClicked = onReportByPhoneClicked,
                            onReportByEmailClicked = onReportByEmailClicked,
                            modifier = Modifier.fillMaxSize()
                        )

                        else -> TODO("Screen not implemented")
                    }
                }
            }
        }

        is InfoUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
    }
}
