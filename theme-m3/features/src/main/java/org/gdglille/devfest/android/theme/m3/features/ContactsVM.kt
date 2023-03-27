package org.gdglille.devfest.android.theme.m3.features

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.data.viewmodels.ContactsUiState
import org.gdglille.devfest.android.data.viewmodels.ContactsViewModel
import org.gdglille.devfest.android.screens.networking.Contacts
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.UserRepository

@Composable
fun ContactsVM(
    userRepository: UserRepository,
    modifier: Modifier = Modifier,
    viewModel: ContactsViewModel = viewModel(
        factory = ContactsViewModel.Factory.create(userRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is ContactsUiState.Loading -> Text(text = stringResource(id = R.string.text_loading))
        is ContactsUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is ContactsUiState.Success -> {
            val usersUi = (uiState.value as ContactsUiState.Success).users
            Contacts(
                users = usersUi,
                modifier = modifier,
                onNetworkDeleted = { viewModel.deleteNetworking(it) }
            )
        }
    }
}
