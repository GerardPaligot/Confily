package org.gdglille.devfest.android.theme.m3.schedules.feature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.android.theme.m3.schedules.screens.ScheduleGridPager
import org.gdglille.devfest.android.theme.m3.style.actions.TopActionsUi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun ScheduleGridVM(
    onScheduleStarted: () -> Unit,
    onFilterClicked: () -> Unit,
    onTalkClicked: (id: String) -> Unit,
    onEventSessionClicked: (id: String) -> Unit,
    showFilterIcon: Boolean,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    isSmallSize: Boolean = false,
    viewModel: ScheduleGridViewModel = koinViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        onScheduleStarted()
    }
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is ScheduleGridUiState.Loading -> ScheduleGridPager(
            agendas = uiState.agenda,
            pagerState = rememberPagerState(pageCount = { 1 }),
            onTalkClicked = {},
            onEventSessionClicked = {},
            onFilterClicked = {},
            onFavoriteClicked = {},
            isLoading = true,
            state = state
        )

        is ScheduleGridUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is ScheduleGridUiState.Success -> ScheduleGridPager(
            agendas = uiState.scheduleUi.schedules,
            topActionsUi = if (!showFilterIcon) TopActionsUi() else uiState.scheduleUi.topActionsUi,
            tabActionsUi = uiState.scheduleUi.tabActionsUi,
            onTalkClicked = onTalkClicked,
            onEventSessionClicked = onEventSessionClicked,
            onFilterClicked = onFilterClicked,
            onFavoriteClicked = { talkItem ->
                viewModel.markAsFavorite(context, talkItem)
            },
            modifier = modifier,
            isSmallSize = isSmallSize,
            isLoading = false,
            state = state
        )
    }
}
