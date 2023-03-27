package org.gdglille.devfest.android.theme.m3.features

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.data.viewmodels.MenusUiState
import org.gdglille.devfest.android.data.viewmodels.MenusViewModel
import org.gdglille.devfest.android.screens.event.Menus
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.AgendaRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenusVM(
    agendaRepository: AgendaRepository,
    modifier: Modifier = Modifier,
    viewModel: MenusViewModel = viewModel(
        factory = MenusViewModel.Factory.create(agendaRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is MenusUiState.Loading -> Menus(
            menuItems = (uiState.value as MenusUiState.Loading).menus,
            modifier = modifier,
            isLoading = true
        )

        is MenusUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is MenusUiState.Success -> Menus(
            menuItems = (uiState.value as MenusUiState.Success).menus,
            modifier = modifier,
            isLoading = false
        )
    }
}
