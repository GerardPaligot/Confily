package com.paligot.conferences.android.screens.event

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paligot.conferences.android.R
import com.paligot.conferences.repositories.AgendaRepository
import com.paligot.conferences.ui.screens.Event

@Composable
fun EventVM(
    agendaRepository: AgendaRepository,
    modifier: Modifier = Modifier,
    onFaqClick: (url: String) -> Unit,
    onCoCClick: (url: String) -> Unit,
    onTwitterClick: (url: String?) -> Unit,
    onLinkedInClick: (url: String?) -> Unit,
    onPartnerClick: (siteUrl: String?) -> Unit
) {
    val viewModel: EventViewModel = viewModel(
        factory = EventViewModel.Factory.create(agendaRepository)
    )
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is EventUiState.Loading -> Text("Loading...")
        is EventUiState.Failure -> Text("Something wrong happened")
        is EventUiState.Success -> Event(
            logo = R.drawable.ic_launcher_foreground,
            event = (uiState.value as EventUiState.Success).event,
            modifier = modifier,
            onFaqClick = onFaqClick,
            onCoCClick = onCoCClick,
            onTwitterClick = onTwitterClick,
            onLinkedInClick = onLinkedInClick,
            onPartnerClick = onPartnerClick
        )
    }
}
