package org.gdglille.devfest.android.theme.m3.features

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.data.viewmodels.PartnerUiState
import org.gdglille.devfest.android.data.viewmodels.PartnersViewModel
import org.gdglille.devfest.android.screens.Partners
import org.gdglille.devfest.repositories.AgendaRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnersVM(
    agendaRepository: AgendaRepository,
    modifier: Modifier = Modifier,
    onPartnerClick: (siteUrl: String?) -> Unit
) {
    val viewModel: PartnersViewModel = viewModel(
        factory = PartnersViewModel.Factory.create(agendaRepository)
    )
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is PartnerUiState.Loading -> Partners(
            partners = (uiState.value as PartnerUiState.Loading).partners,
            modifier = modifier,
            isLoading = true,
            onPartnerClick = {}
        )
        is PartnerUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is PartnerUiState.Success -> Partners(
            partners = (uiState.value as PartnerUiState.Success).partners,
            modifier = modifier,
            onPartnerClick = onPartnerClick
        )
    }
}
