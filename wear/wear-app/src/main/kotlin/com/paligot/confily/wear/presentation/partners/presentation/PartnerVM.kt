package com.paligot.confily.wear.presentation.partners.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paligot.confily.wear.presentation.theme.loading.LoadingPane
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PartnerVM(
    partnerId: String,
    modifier: Modifier = Modifier,
    viewModel: PartnerViewModel = koinViewModel(parameters = { parametersOf(partnerId) })
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState.value) {
        is PartnerUiState.Loading -> LoadingPane()

        is PartnerUiState.Success -> {
            PartnerPane(
                modelUi = state.modelUi,
                modifier = modifier
            )
        }
    }
}
