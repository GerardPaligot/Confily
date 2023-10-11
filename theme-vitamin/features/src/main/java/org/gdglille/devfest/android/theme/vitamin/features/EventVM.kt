package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.theme.m3.infos.feature.EventUiState
import org.gdglille.devfest.android.theme.m3.infos.feature.EventViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.screens.event.Event
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.AgendaRepository

@Composable
fun EventVM(
    agendaRepository: AgendaRepository,
    onLinkClicked: (url: String?) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: org.gdglille.devfest.android.theme.m3.infos.feature.EventViewModel = viewModel(
        factory = org.gdglille.devfest.android.theme.m3.infos.feature.EventViewModel.Factory.create(agendaRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is org.gdglille.devfest.android.theme.m3.infos.feature.EventUiState.Loading -> Event(
            event = (uiState.value as org.gdglille.devfest.android.theme.m3.infos.feature.EventUiState.Loading).event,
            modifier = modifier,
            isLoading = true,
            onLinkClicked = {},
            onItineraryClicked = { _, _ -> }
        )

        is org.gdglille.devfest.android.theme.m3.infos.feature.EventUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is org.gdglille.devfest.android.theme.m3.infos.feature.EventUiState.Success -> Event(
            event = (uiState.value as org.gdglille.devfest.android.theme.m3.infos.feature.EventUiState.Success).event,
            modifier = modifier,
            onLinkClicked = onLinkClicked,
            onItineraryClicked = onItineraryClicked
        )
    }
}
