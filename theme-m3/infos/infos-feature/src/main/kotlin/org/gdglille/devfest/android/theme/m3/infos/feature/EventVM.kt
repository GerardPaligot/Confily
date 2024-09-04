package org.gdglille.devfest.android.theme.m3.infos.feature

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import org.gdglille.devfest.android.theme.m3.infos.screens.Event
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
fun EventVM(
    onLinkClicked: (url: String?) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is EventUiState.Loading -> Event(
            event = uiState.event,
            modifier = modifier,
            isLoading = true,
            onLinkClicked = onLinkClicked,
            onItineraryClicked = { _, _ -> }
        )

        is EventUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is EventUiState.Success -> Event(
            event = uiState.event,
            modifier = modifier,
            onLinkClicked = onLinkClicked,
            onItineraryClicked = onItineraryClicked
        )
    }
}
