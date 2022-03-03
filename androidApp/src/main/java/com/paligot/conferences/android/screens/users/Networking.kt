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
    val viewModel: NetworkingViewModel = viewModel(
        factory = NetworkingViewModel.Factory.create(userRepository)
    )
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is NetworkingUiState.Loading -> Text("Loading...")
        is NetworkingUiState.Failure -> Text("Something wrong happened")
        is NetworkingUiState.Success -> Networking(
            emails = (uiState.value as NetworkingUiState.Success).emails,
            modifier = modifier
        )
    }
}
