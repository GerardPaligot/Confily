package org.gdglille.devfest.android.screens.menus

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.R
import org.gdglille.devfest.android.screens.Menus
import org.gdglille.devfest.repositories.AgendaRepository

@ExperimentalMaterial3Api
@Composable
fun MenusVM(
    agendaRepository: AgendaRepository,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit
) {
    val viewModel: MenusViewModel = viewModel(
        factory = MenusViewModel.Factory.create(agendaRepository)
    )
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is MenusUiState.Loading -> Menus(
            menuItems = (uiState.value as MenusUiState.Loading).menus,
            modifier = modifier,
            isLoading = true,
            onBackClicked = onBackClicked
        )
        is MenusUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is MenusUiState.Success -> Menus(
            menuItems = (uiState.value as MenusUiState.Success).menus,
            modifier = modifier,
            isLoading = false,
            onBackClicked = onBackClicked
        )
    }
}
