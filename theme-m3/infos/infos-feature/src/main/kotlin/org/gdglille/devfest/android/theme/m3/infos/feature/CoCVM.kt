package org.gdglille.devfest.android.theme.m3.infos.feature

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.text_error
import org.gdglille.devfest.android.shared.resources.text_loading
import org.gdglille.devfest.theme.m3.infos.screens.CoCScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CoCVM(
    onReportByPhoneClicked: (String) -> Unit,
    onReportByEmailClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CoCViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is CoCUiState.Loading -> Text(text = stringResource(Resource.string.text_loading))
        is CoCUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is CoCUiState.Success -> CoCScreen(
            coc = uiState.coc,
            modifier = modifier,
            onReportByPhoneClicked = onReportByPhoneClicked,
            onReportByEmailClicked = onReportByEmailClicked
        )
    }
}
