package com.paligot.confily.wear.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paligot.confily.wear.theme.loading.LoadingPane
import org.koin.androidx.compose.koinViewModel

@Composable
fun Main(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState.value) {
        is MainUiState.Loading -> LoadingPane()

        is MainUiState.Success -> {
            MainNavigation(
                startDestination = state.startDestination,
                modifier = modifier
            )
        }
    }
}
