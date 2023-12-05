package org.gdglille.devfest.android.theme.m3.partners.feature

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.theme.m3.partners.screens.PartnersListScreen
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.Scaffold
import org.koin.androidx.compose.koinViewModel

private const val ColumnCountLandscape = 6
private const val ColumnCountPortrait = 3

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PartnersListCompactVM(
    onPartnerClick: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PartnersViewModel = koinViewModel()
) {
    val configuration = LocalConfiguration.current
    val state = rememberLazyGridState()
    val columnCount =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) ColumnCountLandscape
        else ColumnCountPortrait
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(
        title = stringResource(id = R.string.screen_partners),
        modifier = modifier
    ) {
        when (uiState.value) {
            is PartnersUiState.Loading -> PartnersListScreen(
                partners = (uiState.value as PartnersUiState.Loading).partners,
                columnCount = columnCount,
                state = state,
                isLoading = true,
                onPartnerClick = {},
                modifier = Modifier.padding(it)
            )

            is PartnersUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
            is PartnersUiState.Success -> PartnersListScreen(
                partners = (uiState.value as PartnersUiState.Success).partners,
                columnCount = columnCount,
                state = state,
                onPartnerClick = onPartnerClick,
                modifier = Modifier.padding(it)
            )
        }
    }
}
