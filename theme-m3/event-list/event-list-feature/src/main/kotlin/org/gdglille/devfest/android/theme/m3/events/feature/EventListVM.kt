package org.gdglille.devfest.android.theme.m3.events.feature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.repositories.EventRepository

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventListVM(
    repository: EventRepository,
    onEventClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventListViewModel = viewModel(
        factory = EventListViewModel.Factory.create(repository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is EventListUiState.Loading -> EventList(
            events = (uiState.value as EventListUiState.Loading).events,
            modifier = modifier,
            isLoading = true,
            pagerState = rememberPagerState(pageCount = { 1 }),
            onEventClicked = {}
        )

        is EventListUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is EventListUiState.Success -> EventList(
            events = (uiState.value as EventListUiState.Success).events,
            modifier = modifier,
            isLoading = false,
            onEventClicked = {
                viewModel.savedEventId(it)
                onEventClicked()
            }
        )
    }
}
