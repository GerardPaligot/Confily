package com.paligot.confily.infos.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.infos.panes.QAndAListContent
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun QAndAListVM(
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QAndAListViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is QAndAUiState.Loading -> QAndAListContent(
            qAndA = uiState.qanda,
            modifier = modifier,
            isLoading = true,
            onExpandedClicked = {},
            onLinkClicked = onLinkClicked
        )

        is QAndAUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is QAndAUiState.Success -> QAndAListContent(
            qAndA = uiState.qanda,
            modifier = modifier,
            isLoading = false,
            onExpandedClicked = { viewModel.expanded(it) },
            onLinkClicked = onLinkClicked
        )
    }
}
