package org.gdglille.devfest.android.theme.m3.schedules.feature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.AlarmScheduler
import org.gdglille.devfest.android.theme.m3.schedules.screens.ScheduleListOrientable
import org.gdglille.devfest.android.theme.m3.schedules.screens.ScheduleListVerticalScreen
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.actions.TabActionsUi
import org.gdglille.devfest.repositories.AgendaRepository

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun ScheduleListOrientableVM(
    tabs: TabActionsUi,
    agendaRepository: AgendaRepository,
    alarmScheduler: AlarmScheduler,
    pagerState: PagerState,
    onTalkClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScheduleListViewModel = viewModel(
        key = "${tabs.actions.count()}",
        factory = ScheduleListViewModel.Factory.create(
            agendaRepository,
            alarmScheduler
        )
    )
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is ScheduleListUiState.Loading -> ScheduleListVerticalScreen(
            agenda = (uiState.value as ScheduleListUiState.Loading).agenda.first(),
            modifier = modifier,
            isLoading = true,
            onTalkClicked = {},
            onFavoriteClicked = { }
        )

        is ScheduleListUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is ScheduleListUiState.Success -> {
            val agendas = (uiState.value as ScheduleListUiState.Success).agenda
            ScheduleListOrientable(
                agendas = agendas,
                pagerState = pagerState,
                onTalkClicked = onTalkClicked,
                onFavoriteClicked = { talkItem ->
                    viewModel.markAsFavorite(context, talkItem)
                },
                isLoading = false
            )
        }
    }
}
