package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.data.viewmodels.CoCUiState
import org.gdglille.devfest.android.data.viewmodels.CoCViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.screens.event.CoC
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.AgendaRepository

@Composable
fun CoCVM(
    agendaRepository: AgendaRepository,
    onReportByPhoneClicked: (String) -> Unit,
    onReportByEmailClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CoCViewModel = viewModel(
        factory = CoCViewModel.Factory.create(agendaRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is CoCUiState.Loading -> Text(text = stringResource(R.string.text_loading))
        is CoCUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is CoCUiState.Success -> CoC(
            coc = (uiState.value as CoCUiState.Success).coc,
            modifier = modifier,
            onReportByPhoneClicked = onReportByPhoneClicked,
            onReportByEmailClicked = onReportByEmailClicked
        )
    }
}
