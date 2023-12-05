package org.gdglille.devfest.android.theme.m3.partners.feature

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.theme.m3.partners.screens.PartnerDetailOrientable
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.appbars.TopAppBar
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerDetailOrientableVM(
    partnerId: String,
    onLinkClicked: (url: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PartnerDetailViewModel = koinViewModel(parameters = { parametersOf(partnerId) })
) {
    val uiState = viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.screen_partners_detail),
                navigationIcon = { Back(onClick = onBackClicked) },
                scrollBehavior = scrollBehavior
            )
        },
        content = {
            when (uiState.value) {
                is PartnerUiState.Loading -> PartnerDetailOrientable(
                    partnerItemUi = (uiState.value as PartnerUiState.Loading).partner,
                    onLinkClicked = {},
                    onItineraryClicked = { _, _ -> },
                    contentPadding = it,
                    isLoading = true
                )

                is PartnerUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
                is PartnerUiState.Success -> PartnerDetailOrientable(
                    partnerItemUi = (uiState.value as PartnerUiState.Success).partner,
                    onLinkClicked = onLinkClicked,
                    onItineraryClicked = onItineraryClicked,
                    contentPadding = it
                )
            }
        }
    )
}
