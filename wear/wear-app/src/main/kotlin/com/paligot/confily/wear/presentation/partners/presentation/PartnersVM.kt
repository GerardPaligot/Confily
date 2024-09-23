package com.paligot.confily.wear.presentation.partners.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paligot.confily.wear.presentation.theme.loading.LoadingPane
import org.koin.androidx.compose.koinViewModel

@Composable
fun PartnersVM(
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PartnersViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState.value) {
        is PartnersUiState.Loading -> LoadingPane()

        is PartnersUiState.Success -> {
            HorizontalPartnersPager(
                modelUi = state.modelUi,
                modifier = modifier,
                onClick = onClick
            )
        }
    }
}
