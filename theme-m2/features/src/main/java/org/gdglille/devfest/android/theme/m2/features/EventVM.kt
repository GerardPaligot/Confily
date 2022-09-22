package org.gdglille.devfest.android.theme.m2.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.data.viewmodels.EventUiState
import org.gdglille.devfest.android.data.viewmodels.EventViewModel
import org.gdglille.devfest.android.ui.m2.screens.Event
import org.gdglille.devfest.repositories.AgendaRepository

@Composable
fun EventVM(
    agendaRepository: AgendaRepository,
    modifier: Modifier = Modifier,
    onLinkClicked: (url: String?) -> Unit,
    onTicketScannerClicked: () -> Unit,
    onMenusClicked: () -> Unit
) {
    val viewModel: EventViewModel = viewModel(
        factory = EventViewModel.Factory.create(agendaRepository)
    )
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is EventUiState.Loading -> Event(
            event = (uiState.value as EventUiState.Loading).event,
            modifier = modifier,
            isLoading = true,
            onLinkClicked = onLinkClicked,
            onTicketScannerClicked = {}
        ) {}

        is EventUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is EventUiState.Success -> Event(
            event = (uiState.value as EventUiState.Success).event,
            modifier = modifier,
            onLinkClicked = onLinkClicked,
            onTicketScannerClicked = onTicketScannerClicked,
            onMenusClicked = onMenusClicked
        )
    }
}
