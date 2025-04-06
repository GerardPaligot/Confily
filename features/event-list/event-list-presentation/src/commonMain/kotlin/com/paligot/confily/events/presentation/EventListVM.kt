package com.paligot.confily.events.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paligot.confily.events.panes.EventList
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EventListVM(
    onEventLoaded: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventListViewModel = koinViewModel()
) {
    val loadEvent = viewModel.loadEvent.collectAsStateWithLifecycle().value
    LaunchedEffect(loadEvent) {
        if (loadEvent) {
            onEventLoaded()
        }
    }
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is EventListUiState.Loading -> EventList(
            events = uiState.events,
            onEventClicked = {},
            modifier = modifier,
            isLoading = true
        )

        is EventListUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is EventListUiState.Success -> EventList(
            events = uiState.events,
            onEventClicked = viewModel::savedEventId,
            modifier = modifier,
            isLoading = loadEvent
        )
    }
}
