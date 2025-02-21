package com.paligot.confily.mapper.list.panes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.confily.mapper.list.ui.Loading
import com.paligot.confily.mapper.list.ui.SurfacePickFile
import com.paligot.confily.mapper.list.ui.models.MapListUiState

@Composable
fun MapPickerPane(
    uiModel: MapListUiState,
    modifier: Modifier = Modifier,
    onDrag: (List<String>) -> Unit,
    onNameValueChange: (String) -> Unit,
    onOrderValueChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Scaffold(modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            if (uiModel.uiModel.creation == null) {
                SurfacePickFile(
                    label = "Drag and drop an empty map file",
                    onPickFiles = onDrag,
                    modifier = Modifier.padding(24.dp)
                )
            } else {
                MapCreation(
                    uiModel = uiModel.uiModel.creation!!,
                    onNameValueChange = onNameValueChange,
                    onOrderValueChange = onOrderValueChange,
                    onCancelClick = onCancelClick,
                    onSaveClick = onSaveClick,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
        if (uiModel.loading) {
            Loading(modifier = Modifier.fillMaxSize())
        }
    }
}
