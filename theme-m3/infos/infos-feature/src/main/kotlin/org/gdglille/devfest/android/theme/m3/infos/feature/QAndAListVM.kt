package org.gdglille.devfest.android.theme.m3.infos.feature

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.text_error
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun QAndAListVM(
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QAndAListViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is QAndAUiState.Loading -> QAndAList(
            qAndA = uiState.qanda,
            modifier = modifier,
            isLoading = true,
            onExpandedClicked = {},
            onLinkClicked = onLinkClicked
        )

        is QAndAUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is QAndAUiState.Success -> QAndAList(
            qAndA = uiState.qanda,
            modifier = modifier,
            isLoading = false,
            onExpandedClicked = { viewModel.expanded(it) },
            onLinkClicked = onLinkClicked
        )
    }
}
