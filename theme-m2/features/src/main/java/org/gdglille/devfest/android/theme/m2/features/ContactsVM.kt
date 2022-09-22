package org.gdglille.devfest.android.theme.m2.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.data.viewmodels.ContactsUiState
import org.gdglille.devfest.android.data.viewmodels.ContactsViewModel
import org.gdglille.devfest.android.ui.m2.screens.Networking
import org.gdglille.devfest.repositories.UserRepository

@Composable
fun ContactsVM(
    userRepository: UserRepository,
    modifier: Modifier = Modifier
) {
    val viewModel: ContactsViewModel = viewModel(
        factory = ContactsViewModel.Factory.create(userRepository)
    )
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is ContactsUiState.Loading -> Text(text = stringResource(id = R.string.text_loading))
        is ContactsUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is ContactsUiState.Success -> {
            val usersUi = (uiState.value as ContactsUiState.Success).users
            Networking(
                users = usersUi,
                modifier = modifier,
                onNetworkDeleted = { viewModel.deleteNetworking(it) }
            )
        }
    }
}
