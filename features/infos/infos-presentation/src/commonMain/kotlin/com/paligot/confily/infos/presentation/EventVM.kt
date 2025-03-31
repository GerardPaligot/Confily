package com.paligot.confily.infos.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.infos.panes.Event
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EventVM(
    onLinkClicked: (url: String?) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onVersionClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is EventUiState.Loading -> Event(
            event = uiState.event,
            modifier = modifier,
            isLoading = true,
            onLinkClicked = {},
            onItineraryClicked = { _, _ -> },
            onVersionClicked = {}
        )

        is EventUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is EventUiState.Success -> Event(
            event = uiState.event,
            modifier = modifier,
            onLinkClicked = onLinkClicked,
            onItineraryClicked = onItineraryClicked,
            onVersionClicked = onVersionClicked
        )
    }
}
