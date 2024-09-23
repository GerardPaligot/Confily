package com.paligot.confily.wear.presentation.events.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paligot.confily.wear.presentation.theme.errors.NoEventLoaded
import com.paligot.confily.wear.presentation.theme.loading.LoadingPane
import org.koin.androidx.compose.koinViewModel

@Composable
fun CoCVM(
    modifier: Modifier = Modifier,
    viewModel: CoCViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState.value) {
        is CoCUiState.Loading -> LoadingPane()
        is CoCUiState.Failure -> NoEventLoaded()

        is CoCUiState.Success -> {
            CoCPane(
                modelUi = state.modelUi,
                modifier = modifier
            )
        }
    }
}
