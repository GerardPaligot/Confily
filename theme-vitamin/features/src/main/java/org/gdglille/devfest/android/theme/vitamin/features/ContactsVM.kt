package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.theme.m3.networking.feature.ContactsUiState
import org.gdglille.devfest.android.theme.m3.networking.feature.ContactsViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.screens.networking.Contacts
import org.gdglille.devfest.android.theme.vitamin.ui.screens.networking.EmptyContacts
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.UserRepository

@Composable
fun ContactsVM(
    userRepository: UserRepository,
    modifier: Modifier = Modifier,
    viewModel: org.gdglille.devfest.android.theme.m3.networking.feature.ContactsViewModel = viewModel(
        factory = org.gdglille.devfest.android.theme.m3.networking.feature.ContactsViewModel.Factory.create(userRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is org.gdglille.devfest.android.theme.m3.networking.feature.ContactsUiState.Loading -> Text(text = stringResource(id = R.string.text_loading))
        is org.gdglille.devfest.android.theme.m3.networking.feature.ContactsUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is org.gdglille.devfest.android.theme.m3.networking.feature.ContactsUiState.Success -> {
            val usersUi = (uiState.value as org.gdglille.devfest.android.theme.m3.networking.feature.ContactsUiState.Success).users
            if (usersUi.isEmpty()) {
                EmptyContacts()
            } else {
                Contacts(
                    users = usersUi,
                    modifier = modifier,
                    onNetworkDeleted = { viewModel.deleteNetworking(it) }
                )
            }
        }
    }
}
