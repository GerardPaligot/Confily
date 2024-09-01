package org.gdglille.devfest.android.theme.m3.schedules.feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.text_error
import org.gdglille.devfest.android.shared.resources.text_loading
import org.gdglille.devfest.android.theme.m3.schedules.screens.ScheduleDetailOrientableScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDetailOrientableVM(
    scheduleId: String,
    onBackClicked: () -> Unit,
    onSpeakerClicked: (id: String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    modifier: Modifier = Modifier,
    isLandscape: Boolean = false,
    viewModel: ScheduleDetailViewModel = koinViewModel(parameters = { parametersOf(scheduleId) })
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        content = {
            when (val uiState = viewModel.uiState.collectAsState().value) {
                is ScheduleUiState.Loading -> Text(text = stringResource(Resource.string.text_loading))
                is ScheduleUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
                is ScheduleUiState.Success -> ScheduleDetailOrientableScreen(
                    talk = uiState.talk,
                    onBackClicked = onBackClicked,
                    onSpeakerClicked = onSpeakerClicked,
                    onShareClicked = onShareClicked,
                    modifier = modifier,
                    isLandscape = isLandscape
                )
            }
        }
    )
}
