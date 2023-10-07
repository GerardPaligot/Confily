package org.gdglille.devfest.android.theme.m3.features

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.data.viewmodels.AgendaFiltersUiState
import org.gdglille.devfest.android.data.viewmodels.AgendaFiltersViewModel
import org.gdglille.devfest.android.screens.agenda.AgendaFilters
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.AgendaRepository

@Composable
fun AgendaFiltersVM(
    agendaRepository: AgendaRepository,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AgendaFiltersViewModel = viewModel(
        factory = AgendaFiltersViewModel.Factory.create(agendaRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is AgendaFiltersUiState.Loading -> Text(text = stringResource(id = R.string.text_loading))
        is AgendaFiltersUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is AgendaFiltersUiState.Success -> {
            val filtersUi = (uiState.value as AgendaFiltersUiState.Success).filters
            AgendaFilters(
                filtersUi = filtersUi,
                onFavoriteClick = viewModel::applyFavoriteFilter,
                onCategoryClick = viewModel::applyCategoryFilter,
                onFormatClick = viewModel::applyFormatFilter,
                onBack = onBackClicked,
                modifier = modifier
            )
        }
    }
}
