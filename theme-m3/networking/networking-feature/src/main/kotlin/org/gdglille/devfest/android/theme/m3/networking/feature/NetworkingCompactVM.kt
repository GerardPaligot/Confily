package org.gdglille.devfest.android.theme.m3.networking.feature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.theme.m3.navigation.ActionIds
import org.gdglille.devfest.android.theme.m3.navigation.TabActions
import org.gdglille.devfest.android.theme.m3.networking.screens.EmptyNetworkingScreen
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.Scaffold
import org.gdglille.devfest.models.ui.ExportNetworkingUi
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NetworkingCompactVM(
    onCreateProfileClicked: () -> Unit,
    onContactScannerClicked: () -> Unit,
    onContactExportClicked: (ExportNetworkingUi) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NetworkingViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val exportPath = viewModel.exportPath.collectAsState(null)
    LaunchedEffect(exportPath.value) {
        exportPath.value?.let(onContactExportClicked)
    }
    val title = stringResource(id = R.string.screen_networking)
    when (uiState.value) {
        is NetworkingUiState.Loading -> Scaffold(title = title, modifier = modifier) {
            EmptyNetworkingScreen()
        }

        is NetworkingUiState.Success -> {
            val uiModel = uiState.value as NetworkingUiState.Success
            val pagerState: PagerState =
                rememberPagerState(pageCount = { uiModel.tabActionsUi.actions.count() })
            LaunchedEffect(pagerState.currentPage) {
                viewModel.innerScreenConfig(uiModel.tabActionsUi.actions[pagerState.currentPage].route)
            }
            Scaffold(
                title = title,
                topActions = uiModel.topActionsUi,
                tabActions = uiModel.tabActionsUi,
                fabAction = uiModel.fabAction,
                onActionClicked = {
                    when (it.id) {
                        ActionIds.EXPORT -> {
                            viewModel.exportNetworking()
                        }
                    }
                },
                onFabActionClicked = {
                    when (it.id) {
                        ActionIds.CREATE_PROFILE -> {
                            onCreateProfileClicked()
                        }

                        ActionIds.SCAN_CONTACTS -> {
                            onContactScannerClicked()
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
                    when (uiModel.tabActionsUi.actions[page].route) {
                        TabActions.myProfile.route -> MyProfileCompactVM(
                            onEditInformation = onCreateProfileClicked
                        )

                        TabActions.contacts.route -> ContactsCompactVM()

                        else -> TODO("Screen not implemented")
                    }
                }
            }
        }

        is NetworkingUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
    }
}
