package org.gdglille.devfest.android.theme.m3.schedules.feature

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.AlarmScheduler
import org.gdglille.devfest.android.theme.m3.navigation.ActionIds
import org.gdglille.devfest.android.theme.m3.schedules.screens.ScheduleListHorizontalPager
import org.gdglille.devfest.android.theme.m3.schedules.screens.ScheduleListVerticalPager
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.Scaffold
import org.gdglille.devfest.repositories.AgendaRepository

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun ScheduleListCompactVM(
    agendaRepository: AgendaRepository,
    alarmScheduler: AlarmScheduler,
    onFilterClicked: () -> Unit,
    onTalkClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScheduleListViewModel = viewModel(
        factory = ScheduleListViewModel.Factory.create(agendaRepository, alarmScheduler)
    )
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val title = stringResource(id = R.string.screen_agenda)
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is ScheduleListUiState.Loading -> Scaffold(
            title = title,
            modifier = modifier
        ) {
            ScheduleListVerticalPager(
                agendas = (uiState.value as ScheduleListUiState.Loading).agenda,
                onTalkClicked = {},
                onFavoriteClicked = {},
                isLoading = true
            )
        }

        is ScheduleListUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is ScheduleListUiState.Success -> {
            val modelUi = (uiState.value as ScheduleListUiState.Success)
            val count = modelUi.scheduleUi.tabActionsUi.actions.count()
            val pagerState = rememberPagerState(pageCount = { count })
            Scaffold(
                title = title,
                modifier = modifier,
                topActions = modelUi.scheduleUi.topActionsUi,
                tabActions = modelUi.scheduleUi.tabActionsUi,
                onActionClicked = {
                    when (it.id) {
                        ActionIds.FILTERS -> {
                            onFilterClicked()
                        }
                    }
                }
            ) {
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    ScheduleListHorizontalPager(
                        agendas = modelUi.scheduleUi.schedules,
                        pagerState = pagerState,
                        onTalkClicked = onTalkClicked,
                        onFavoriteClicked = { talkItem ->
                            viewModel.markAsFavorite(context, talkItem)
                        },
                        isLoading = false,
                        modifier = Modifier.padding(it)
                    )
                } else {
                    ScheduleListVerticalPager(
                        agendas = modelUi.scheduleUi.schedules,
                        pagerState = pagerState,
                        onTalkClicked = onTalkClicked,
                        onFavoriteClicked = { talkItem ->
                            viewModel.markAsFavorite(context, talkItem)
                        },
                        isLoading = false,
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }
    }
}
