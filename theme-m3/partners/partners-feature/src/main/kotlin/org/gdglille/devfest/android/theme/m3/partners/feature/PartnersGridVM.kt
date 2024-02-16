package org.gdglille.devfest.android.theme.m3.partners.feature

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.theme.m3.partners.screens.PartnersGridScreen
import org.gdglille.devfest.android.theme.m3.style.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun PartnersGridVM(
    onPartnerClick: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PartnersViewModel = koinViewModel()
) {
    val state = rememberLazyGridState()
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is PartnersUiState.Loading -> PartnersGridScreen(
            partners = uiState.partners,
            state = state,
            isLoading = true,
            onPartnerClick = {},
            modifier = modifier
        )

        is PartnersUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is PartnersUiState.Success -> PartnersGridScreen(
            partners = uiState.partners,
            state = state,
            onPartnerClick = onPartnerClick,
            modifier = modifier
        )
    }
}
