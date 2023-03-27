package org.gdglille.devfest.android.theme.m3.features

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.data.viewmodels.PartnerUiState
import org.gdglille.devfest.android.data.viewmodels.PartnerViewModel
import org.gdglille.devfest.android.screens.partners.PartnerDetail
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.AgendaRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerDetailVM(
    partnerId: String,
    agendaRepository: AgendaRepository,
    onLinkClicked: (url: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PartnerViewModel = viewModel(
        factory = PartnerViewModel.Factory.create(partnerId, agendaRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is PartnerUiState.Loading -> PartnerDetail(
            partnerItemUi = (uiState.value as PartnerUiState.Loading).partner,
            modifier = modifier,
            isLoading = true,
            onLinkClicked = {},
            onItineraryClicked = { _, _ -> },
            onBackClicked = onBackClicked
        )

        is PartnerUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is PartnerUiState.Success -> PartnerDetail(
            partnerItemUi = (uiState.value as PartnerUiState.Success).partner,
            modifier = modifier,
            onLinkClicked = onLinkClicked,
            onItineraryClicked = onItineraryClicked,
            onBackClicked = onBackClicked
        )
    }
}
