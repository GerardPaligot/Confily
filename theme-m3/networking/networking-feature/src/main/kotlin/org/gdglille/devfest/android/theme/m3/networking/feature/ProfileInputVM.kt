package org.gdglille.devfest.android.theme.m3.networking.feature

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.theme.m3.networking.screens.ProfileInputScreen
import org.gdglille.devfest.android.theme.m3.style.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInputVM(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileInputViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is ProfileInputUiState.Loading -> Text(text = stringResource(id = R.string.text_loading))
        is ProfileInputUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is ProfileInputUiState.Success -> ProfileInputScreen(
            profile = uiState.profile,
            modifier = modifier,
            onBackClicked = onBackClicked,
            onValueChanged = viewModel::fieldChanged,
            onValidation = {
                viewModel.saveProfile()
                onBackClicked()
            }
        )
    }
}
