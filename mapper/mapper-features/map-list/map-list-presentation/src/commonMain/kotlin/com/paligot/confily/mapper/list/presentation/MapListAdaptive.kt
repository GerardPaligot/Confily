package com.paligot.confily.mapper.list.presentation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paligot.confily.mapper.list.panes.MapListPane
import com.paligot.confily.mapper.list.panes.MapPickerPane
import com.paligot.confily.mapper.list.ui.models.MapListUiState

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MapListAdaptive(
    uiModel: MapListUiState,
    modifier: Modifier = Modifier,
    onDrag: (List<String>) -> Unit,
    onNameValueChange: (String) -> Unit,
    onOrderValueChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
    onItemClick: (String) -> Unit
) {
    val navigator = rememberSupportingPaneScaffoldNavigator()
    SupportingPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        modifier = modifier,
        mainPane = {
            MapPickerPane(
                uiModel = uiModel,
                modifier = modifier,
                onDrag = onDrag,
                onNameValueChange = onNameValueChange,
                onOrderValueChange = onOrderValueChange,
                onCancelClick = onCancelClick,
                onSaveClick = onSaveClick
            )
        },
        supportingPane = {
            MapListPane(
                mapItems = uiModel.uiModel.items,
                onClick = onItemClick
            )
        }
    )
}
