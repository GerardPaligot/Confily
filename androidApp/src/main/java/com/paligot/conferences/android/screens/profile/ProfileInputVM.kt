package com.paligot.conferences.android.screens.profile

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paligot.conferences.android.R
import com.paligot.conferences.repositories.UserRepository
import com.paligot.conferences.ui.screens.ProfileInput

@Composable
fun ProfileInputVM(
    userRepository: UserRepository,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
) {
    val viewModel: ProfileInputViewModel = viewModel(
        factory = ProfileInputViewModel.Factory.create(userRepository)
    )
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is ProfileInputUiState.Loading -> Text(text = stringResource(id = R.string.text_loading))
        is ProfileInputUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is ProfileInputUiState.Success -> ProfileInput(
            profile = (uiState.value as ProfileInputUiState.Success).profile,
            modifier = modifier,
            onBackClicked = onBackClicked,
            onValueChanged = { field, input ->
                viewModel.fieldChanged(field, input)
            },
            onValidation = {
                viewModel.saveProfile()
            }
        )
    }
}
