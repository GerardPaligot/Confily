package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.data.viewmodels.QAndAUiState
import org.gdglille.devfest.android.data.viewmodels.QAndAViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.screens.event.QAndAList
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.AgendaRepository

@Composable
fun QAndAListVM(
    agendaRepository: AgendaRepository,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QAndAViewModel = viewModel(
        factory = QAndAViewModel.Factory.create(agendaRepository)
    )
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
