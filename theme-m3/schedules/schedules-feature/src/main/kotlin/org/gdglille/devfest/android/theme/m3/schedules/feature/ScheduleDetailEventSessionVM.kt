package org.gdglille.devfest.android.theme.m3.schedules.feature

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.text_error
import org.gdglille.devfest.android.shared.resources.text_loading
import org.gdglille.devfest.android.theme.m3.schedules.screens.ScheduleDetailEventSessionScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun ScheduleDetailEventSessionVM(
    scheduleId: String,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScheduleDetailEventSessionViewModel =
        koinViewModel(parameters = { parametersOf(scheduleId) })
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is ScheduleEventUiState.Loading -> Text(text = stringResource(Resource.string.text_loading))
        is ScheduleEventUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is ScheduleEventUiState.Success -> ScheduleDetailEventSessionScreen(
            session = uiState.session,
            modifier = modifier,
            onBackClicked = onBackClicked
        )
    }
}
