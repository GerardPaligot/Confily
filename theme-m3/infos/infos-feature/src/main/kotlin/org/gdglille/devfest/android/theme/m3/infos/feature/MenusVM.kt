package org.gdglille.devfest.android.theme.m3.infos.feature

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.theme.m3.style.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenusVM(
    modifier: Modifier = Modifier,
    viewModel: MenusViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is MenusUiState.Loading -> Menus(
            menuItems = uiState.menus,
            modifier = modifier,
            isLoading = true
        )

        is MenusUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is MenusUiState.Success -> Menus(
            menuItems = uiState.menus,
            modifier = modifier,
            isLoading = false
        )
    }
}
