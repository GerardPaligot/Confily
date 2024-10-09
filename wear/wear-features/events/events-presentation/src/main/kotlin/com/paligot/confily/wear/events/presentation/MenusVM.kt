package com.paligot.confily.wear.events.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paligot.confily.wear.events.panes.MenusPane
import com.paligot.confily.wear.theme.errors.NoEventLoaded
import com.paligot.confily.wear.theme.loading.LoadingPane
import org.koin.androidx.compose.koinViewModel

@Composable
fun MenusVM(
    modifier: Modifier = Modifier,
    viewModel: MenusViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState.value) {
        is MenusUiState.Loading -> LoadingPane()
        is MenusUiState.Failure -> NoEventLoaded()

        is MenusUiState.Success -> {
            MenusPane(
                modelUi = state.modelUi,
                modifier = modifier
            )
        }
    }
}
