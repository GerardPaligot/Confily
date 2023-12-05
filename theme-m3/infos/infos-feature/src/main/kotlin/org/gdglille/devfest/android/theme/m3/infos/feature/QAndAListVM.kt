package org.gdglille.devfest.android.theme.m3.infos.feature

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.theme.m3.style.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun QAndAListVM(
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QAndAListViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is QAndAUiState.Loading -> QAndAList(
            qAndA = (uiState.value as QAndAUiState.Loading).qanda,
            modifier = modifier,
            isLoading = true,
            onExpandedClicked = {},
            onLinkClicked = onLinkClicked
        )

        is QAndAUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is QAndAUiState.Success -> QAndAList(
            qAndA = (uiState.value as QAndAUiState.Success).qanda,
            modifier = modifier,
            isLoading = false,
            onExpandedClicked = {
                viewModel.expanded(it)
            },
            onLinkClicked = onLinkClicked
        )
    }
}
