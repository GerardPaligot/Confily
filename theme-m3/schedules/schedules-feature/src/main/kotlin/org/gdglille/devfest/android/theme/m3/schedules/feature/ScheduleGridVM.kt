package org.gdglille.devfest.android.theme.m3.schedules.feature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.android.theme.m3.navigation.ActionIds
import org.gdglille.devfest.android.theme.m3.schedules.screens.ScheduleGridPager
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.Scaffold
import org.gdglille.devfest.android.theme.m3.style.actions.TopActionsUi
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun ScheduleGridVM(
    onScheduleStarted: () -> Unit,
    onFilterClicked: () -> Unit,
    onTalkClicked: (id: String) -> Unit,
    showFilterIcon: Boolean,
    modifier: Modifier = Modifier,
    isSmallSize: Boolean = false,
    viewModel: ScheduleGridViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val title = stringResource(id = R.string.screen_agenda)
    LaunchedEffect(key1 = Unit) {
        onScheduleStarted()
    }
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is ScheduleGridUiState.Loading -> Scaffold(
            title = title,
            modifier = modifier
        ) {
            ScheduleGridPager(
                agendas = uiState.agenda,
                onTalkClicked = {},
                onFavoriteClicked = {},
                isLoading = true
            )
        }

        is ScheduleGridUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is ScheduleGridUiState.Success -> {
            val count = uiState.scheduleUi.tabActionsUi.actions.count()
            val pagerState = rememberPagerState(pageCount = { count })
            Scaffold(
                title = title,
                modifier = modifier,
                topActions = if (!showFilterIcon) TopActionsUi() else uiState.scheduleUi.topActionsUi,
                tabActions = uiState.scheduleUi.tabActionsUi,
                hasScrollBehavior = false,
                onActionClicked = {
                    when (it.id) {
                        ActionIds.FILTERS -> {
                            onFilterClicked()
                        }
                    }
                }
            ) {
                ScheduleGridPager(
                    agendas = uiState.scheduleUi.schedules,
                    onTalkClicked = onTalkClicked,
                    onFavoriteClicked = { talkItem ->
                        viewModel.markAsFavorite(context, talkItem)
                    },
                    modifier = Modifier.padding(top = it.calculateTopPadding()),
                    pagerState = pagerState,
                    isSmallSize = isSmallSize,
                    isLoading = false
                )
            }
        }
    }
}
