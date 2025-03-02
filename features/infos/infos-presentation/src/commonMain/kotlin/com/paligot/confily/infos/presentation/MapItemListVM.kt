package com.paligot.confily.infos.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.infos.panes.MapItemListContent
import com.paligot.confily.infos.ui.models.MapItemUiState
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MapItemListVM(
    modifier: Modifier = Modifier,
    viewModel: MapItemListViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is MapItemListUiState.Loading -> MapItemListContent(
            uiState = MapItemUiState(),
            modifier = modifier,
            onFilterClick = viewModel::filterClick
        )

        is MapItemListUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is MapItemListUiState.Success -> MapItemListContent(
            uiState = uiState.uiState,
            modifier = modifier,
            onFilterClick = viewModel::filterClick
        )
    }
}
