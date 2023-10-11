package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.theme.m3.networking.feature.ProfileInputUiState
import org.gdglille.devfest.android.theme.m3.networking.feature.ProfileInputViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.screens.networking.ProfileInput
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.UserRepository

@Composable
fun ProfileInputVM(
    userRepository: UserRepository,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: org.gdglille.devfest.android.theme.m3.networking.feature.ProfileInputViewModel = viewModel(
        factory = org.gdglille.devfest.android.theme.m3.networking.feature.ProfileInputViewModel.Factory.create(userRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is org.gdglille.devfest.android.theme.m3.networking.feature.ProfileInputUiState.Loading -> Text(text = stringResource(id = R.string.text_loading))
        is org.gdglille.devfest.android.theme.m3.networking.feature.ProfileInputUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is org.gdglille.devfest.android.theme.m3.networking.feature.ProfileInputUiState.Success -> ProfileInput(
            profile = (uiState.value as org.gdglille.devfest.android.theme.m3.networking.feature.ProfileInputUiState.Success).profile,
            modifier = modifier,
            onBackClicked = onBackClicked,
            onValueChanged = { field, input ->
                viewModel.fieldChanged(field, input)
            },
            onValidation = {
                viewModel.saveProfile()
                onBackClicked()
            }
        )
    }
}
