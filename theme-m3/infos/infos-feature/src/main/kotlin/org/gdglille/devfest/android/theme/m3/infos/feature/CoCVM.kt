package org.gdglille.devfest.android.theme.m3.infos.feature

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.theme.m3.style.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun CoCVM(
    onReportByPhoneClicked: (String) -> Unit,
    onReportByEmailClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CoCViewModel = koinViewModel()
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
