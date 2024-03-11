package org.gdglille.devfest.android.theme.m3.schedules.feature

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import io.openfeedback.android.viewmodels.OpenFeedbackFirebaseConfig
import org.gdglille.devfest.android.theme.m3.schedules.screens.ScheduleDetailOrientableScreen
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.text_error
import org.gdglille.devfest.android.shared.resources.text_loading
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun ScheduleDetailOrientableVM(
    scheduleId: String,
    openfeedbackFirebaseConfig: OpenFeedbackFirebaseConfig,
    onBackClicked: () -> Unit,
    onSpeakerClicked: (id: String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScheduleDetailViewModel = koinViewModel(parameters = { parametersOf(scheduleId) })
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is ScheduleUiState.Loading -> Text(text = stringResource(Resource.string.text_loading))
        is ScheduleUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is ScheduleUiState.Success -> ScheduleDetailOrientableScreen(
            talk = uiState.talk,
            openFeedbackFirebaseConfig = openfeedbackFirebaseConfig,
            modifier = modifier,
            onBackClicked = onBackClicked,
            onSpeakerClicked = onSpeakerClicked,
            onShareClicked = onShareClicked
        )
    }
}
