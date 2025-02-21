package com.paligot.confily.mapper.detail.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.mapper.detail.panes.MapPane
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MapDetailVM(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MapDetailViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    MapPane(
        uiState = uiState,
        modifier = modifier,
        onPress = viewModel::press,
        onRelease = viewModel::end,
        onMove = viewModel::move,
        onPickEmptyFiles = viewModel::pickEmptyFiles,
        onPickFilledFiles = viewModel::pickFilledFiles,
        onNameValueChange = viewModel::nameChange,
        onColorValueChange = viewModel::colorChange,
        onColorSelectedValueChange = viewModel::colorSelectedChange,
        onShapeOrderValueChange = viewModel::shapeOrderChange,
        onShapeValueChange = viewModel::shapeNameChange,
        onDescriptionValueChange = viewModel::shapeDescriptionChange,
        onPictogramSizeValueChange = viewModel::pictogramSizeChange,
        onModeClick = viewModel::modeClick,
        onTypeClick = viewModel::typeClick,
        onPictogramClick = viewModel::pictogramClick,
        onCloseClick = onBack,
        onSaveClick = viewModel::save
    )
}
