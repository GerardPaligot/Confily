package com.paligot.confily.networking.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.networking.panes.ContactsScreen
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import com.paligot.confily.resources.text_loading
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ContactsCompactVM(
    modifier: Modifier = Modifier,
    viewModel: ContactsViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is ContactsUiState.Loading -> Text(text = stringResource(Resource.string.text_loading))
        is ContactsUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is ContactsUiState.Success -> {
            ContactsScreen(
                users = uiState.users,
                modifier = modifier,
                onNetworkDeleted = viewModel::deleteNetworking
            )
        }
    }
}
