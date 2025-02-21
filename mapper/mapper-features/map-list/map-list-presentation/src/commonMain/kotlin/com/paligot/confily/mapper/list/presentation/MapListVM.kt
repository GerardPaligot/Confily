package com.paligot.confily.mapper.list.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MapListVM(
    eventId: String,
    apiKey: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MapListViewModel = koinViewModel(parameters = { parametersOf(eventId, apiKey) })
) {
    val uiState = viewModel.uiState.collectAsState().value
    MapListAdaptive(
        uiModel = uiState,
        modifier = modifier,
        onDrag = viewModel::dropMap,
        onNameValueChange = viewModel::nameChange,
        onOrderValueChange = viewModel::orderChange,
        onCancelClick = viewModel::cancelCreation,
        onSaveClick = viewModel::saveCreation,
        onItemClick = onItemClick
    )
}
