package org.gdglille.devfest.android.theme.m3.schedules.feature

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.theme.m3.schedules.screens.AgendaFiltersScreen
import org.gdglille.devfest.android.theme.m3.style.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun AgendaFiltersVM(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AgendaFiltersViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is AgendaFiltersUiState.Loading -> Text(text = stringResource(id = R.string.text_loading))
        is AgendaFiltersUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is AgendaFiltersUiState.Success -> {
            val filtersUi = (uiState.value as AgendaFiltersUiState.Success).filters
            AgendaFiltersScreen(
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
