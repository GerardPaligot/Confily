package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import org.gdglille.devfest.android.data.viewmodels.EventListUiState
import org.gdglille.devfest.android.data.viewmodels.EventListViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.screens.events.EventList
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.EventRepository

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EventListVM(
    repository: EventRepository,
    onEventClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventListViewModel = viewModel(
        factory = EventListViewModel.Factory.create(repository)
    )
) {
    val pagerState = rememberPagerState()
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is EventListUiState.Loading -> EventList(
            events = (uiState.value as EventListUiState.Loading).events,
            modifier = modifier,
            pagerState = pagerState,
            isLoading = true,
            onEventClicked = {}
        )

        is EventListUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is EventListUiState.Success -> EventList(
            events = (uiState.value as EventListUiState.Success).events,
            modifier = modifier,
            pagerState = pagerState,
            isLoading = false,
            onEventClicked = {
                viewModel.savedEventId(it)
                onEventClicked()
            }
        )
    }
}
