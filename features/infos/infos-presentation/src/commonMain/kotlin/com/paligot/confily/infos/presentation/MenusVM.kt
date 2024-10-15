package com.paligot.confily.infos.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.infos.panes.MenusScreen
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenusVM(
    modifier: Modifier = Modifier,
    viewModel: MenusViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is MenusUiState.Loading -> MenusScreen(
            menuItems = uiState.menus,
            modifier = modifier,
            isLoading = true
        )

        is MenusUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is MenusUiState.Success -> MenusScreen(
            menuItems = uiState.menus,
            modifier = modifier,
            isLoading = false
        )
    }
}
