package com.paligot.confily.events.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.events.panes.EventList
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EventListVM(
    onEventClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventListViewModel = koinViewModel()
) {
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
            onEventClicked = {
                viewModel.savedEventId(it)
                onEventClicked()
            },
            modifier = modifier,
            isLoading = false
        )
    }
}
