package org.gdglille.devfest.android.theme.m3.partners.feature

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.partners.panes.PartnersGridScreen
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PartnersGridVM(
    onPartnerClick: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    viewModel: PartnersViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is PartnersUiState.Loading -> PartnersGridScreen(
            partners = uiState.partners,
            state = state,
            isLoading = true,
            onPartnerClick = {},
            modifier = modifier
        )

        is PartnersUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is PartnersUiState.Success -> PartnersGridScreen(
            partners = uiState.partners,
            state = state,
            onPartnerClick = onPartnerClick,
            modifier = modifier
        )
    }
}
