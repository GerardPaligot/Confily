package org.gdglille.devfest.android.theme.m3.features

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.data.viewmodels.EventListUiState
import org.gdglille.devfest.android.data.viewmodels.EventListViewModel
import org.gdglille.devfest.android.screens.events.EventList
import org.gdglille.devfest.repositories.EventRepository

@Composable
fun EventListVM(
    repository: EventRepository,
    modifier: Modifier = Modifier,
    onEventClicked: (String) -> Unit
) {
    val viewModel: EventListViewModel = viewModel(
        factory = EventListViewModel.Factory.create(repository)
    )
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is EventListUiState.Loading -> EventList(
            events = (uiState.value as EventListUiState.Loading).events,
            modifier = modifier,
            isLoading = true,
            onEventClicked = onEventClicked
        )

        is EventListUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is EventListUiState.Success -> EventList(
            events = (uiState.value as EventListUiState.Success).events,
            modifier = modifier,
            isLoading = false,
            onEventClicked = onEventClicked
        )
    }
}
