package org.gdglille.devfest.android.theme.m3.networking.feature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
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
import org.gdglille.devfest.android.theme.m3.networking.screens.EmptyNetworkingScreen
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.appbars.TopAppBarContentLayout
import org.gdglille.devfest.models.ui.ExportNetworkingUi
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.UserRepository

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NetworkingCompactVM(
    agendaRepository: AgendaRepository,
    userRepository: UserRepository,
    onCreateProfileClicked: () -> Unit,
    onContactExportClicked: (ExportNetworkingUi) -> Unit,
    onInnerScreenOpened: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NetworkingViewModel = viewModel(
        factory = NetworkingViewModel.Factory.create(agendaRepository, userRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    val exportPath = viewModel.exportPath.collectAsState(null)
    LaunchedEffect(exportPath.value) {
        exportPath.value?.let(onContactExportClicked)
    }
    val title = stringResource(id = R.string.screen_networking)
    when (uiState.value) {
        is NetworkingUiState.Loading -> TopAppBarContentLayout(title = title, modifier = modifier) {
            EmptyNetworkingScreen()
        }

        is NetworkingUiState.Success -> {
            val uiModel = uiState.value as NetworkingUiState.Success
            val pagerState: PagerState =
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
                        ActionIds.EXPORT -> {
                            viewModel.exportNetworking()
                        }
                    }
                },
                pagerState = pagerState
            ) {
                HorizontalPager(state = pagerState) { page ->
                    when (uiModel.tabActionsUi.actions[page].route) {
                        TabActions.myProfile.route -> MyProfileCompactVM(
                            userRepository = userRepository,
                            onEditInformation = onCreateProfileClicked
                        )

                        TabActions.contacts.route -> ContactsCompactVM(userRepository = userRepository)

                        else -> TODO("Screen not implemented")
                    }
                }
            }
        }

        is NetworkingUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
    }
}
