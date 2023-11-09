package org.gdglille.devfest.android.theme.m3.partners.feature

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.theme.m3.partners.screens.PartnerDetailOrientable
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.appbars.TopAppBar
import org.gdglille.devfest.repositories.AgendaRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerDetailOrientableVM(
    partnerId: String,
    agendaRepository: AgendaRepository,
    onLinkClicked: (url: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PartnerDetailViewModel = viewModel(
        factory = PartnerDetailViewModel.Factory.create(partnerId, agendaRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.screen_partners_detail),
                navigationIcon = { Back(onClick = onBackClicked) }
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
