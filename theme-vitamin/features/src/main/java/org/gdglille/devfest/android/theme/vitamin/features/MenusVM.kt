package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.theme.m3.infos.feature.MenusUiState
import org.gdglille.devfest.android.theme.m3.infos.feature.MenusViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.screens.event.Menus
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.AgendaRepository

@Composable
fun MenusVM(
    agendaRepository: AgendaRepository,
    modifier: Modifier = Modifier,
    viewModel: org.gdglille.devfest.android.theme.m3.infos.feature.MenusViewModel = viewModel(
        factory = org.gdglille.devfest.android.theme.m3.infos.feature.MenusViewModel.Factory.create(agendaRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is org.gdglille.devfest.android.theme.m3.infos.feature.MenusUiState.Loading -> Menus(
            menuItems = (uiState.value as org.gdglille.devfest.android.theme.m3.infos.feature.MenusUiState.Loading).menus,
            modifier = modifier,
            isLoading = true
        )
        is org.gdglille.devfest.android.theme.m3.infos.feature.MenusUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is org.gdglille.devfest.android.theme.m3.infos.feature.MenusUiState.Success -> Menus(
            menuItems = (uiState.value as org.gdglille.devfest.android.theme.m3.infos.feature.MenusUiState.Success).menus,
            modifier = modifier,
            isLoading = false
        )
    }
}
