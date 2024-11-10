package com.paligot.confily.partners.presentation

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.partners.panes.PartnersActivitiesPager
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PartnersGridVM(
    onPartnerClick: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    viewModel: PartnersViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is PartnersUiState.Loading -> PartnersActivitiesPager(
            uiModel = uiState.uiModel,
            isLoading = true,
            onPartnerClick = {},
            modifier = modifier,
            partnersState = state
        )

        is PartnersUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is PartnersUiState.Success -> PartnersActivitiesPager(
            uiModel = uiState.uiModel,
            tabActionsUi = uiState.tabActionsUi,
            isLoading = false,
            onPartnerClick = onPartnerClick,
            modifier = modifier,
            partnersState = state
        )
    }
}
