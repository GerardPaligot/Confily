package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.data.viewmodels.ProfileInputUiState
import org.gdglille.devfest.android.data.viewmodels.ProfileInputViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.screens.networking.EmptyNetworking
import org.gdglille.devfest.android.theme.vitamin.ui.screens.networking.MyProfile
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.UserRepository

@Composable
fun MyProfileVM(
    userRepository: UserRepository,
    onEditInformation: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileInputViewModel = viewModel(
        factory = ProfileInputViewModel.Factory.create(userRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is ProfileInputUiState.Loading -> Text(text = stringResource(id = R.string.text_loading))
        is ProfileInputUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is ProfileInputUiState.Success -> {
            val profileUi = (uiState.value as ProfileInputUiState.Success).profile
            if (profileUi.qrCode == null) {
                EmptyNetworking()
            } else {
                MyProfile(
                    profileUi = profileUi,
                    modifier = modifier,
                    onEditInformation = onEditInformation
                )
            }
        }
    }
}
