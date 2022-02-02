package com.paligot.conferences.android.screens.users

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paligot.conferences.repositories.UserRepository
import com.paligot.conferences.ui.screens.Networking

@Composable
fun NetworkingVM(
    userRepository: UserRepository,
    modifier: Modifier = Modifier
) {
    val viewModel: UserProfileViewModel = viewModel(
        factory = UserProfileViewModel.Factory.create(userRepository)
    )
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is ProfileUiState.Loading -> Text("Loading...")
        is ProfileUiState.Failure -> Text("Something wrong happened")
        is ProfileUiState.Success -> Networking(
            profileUi = (uiState.value as ProfileUiState.Success).profile,
            imageBitmap = (uiState.value as ProfileUiState.Success).imageBitmap,
            modifier = modifier,
            onValueChanged = {
                viewModel.emailChanged(it)
            },
            onValidation = {
                viewModel.fetchNewEmailQrCode()
            },
            onQrCodeClicked = {
                viewModel.displayQrCode()
            },
            onDismissRequest = {
                viewModel.closeQrCode()
            }
        )
    }
}
