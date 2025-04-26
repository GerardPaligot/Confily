package com.paligot.confily.networking.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.networking.panes.ProfileInputPane
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import com.paligot.confily.resources.text_loading
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInputVM(
    onBackClicked: () -> Unit,
    onProfileCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileInputViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is ProfileInputUiState.Loading -> Text(text = stringResource(Resource.string.text_loading))
        is ProfileInputUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is ProfileInputUiState.Success -> ProfileInputPane(
            profile = uiState.profile,
            modifier = modifier,
            onBackClicked = onBackClicked,
            onValueChanged = viewModel::fieldChanged,
            onValidation = {
                viewModel.saveProfile()
                onProfileCreated()
            }
        )
    }
}
