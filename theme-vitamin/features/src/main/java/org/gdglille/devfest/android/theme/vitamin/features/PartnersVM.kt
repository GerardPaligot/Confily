package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.data.viewmodels.PartnersUiState
import org.gdglille.devfest.android.data.viewmodels.PartnersViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.screens.partners.Partners
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.AgendaRepository

@Composable
fun PartnersVM(
    agendaRepository: AgendaRepository,
    onPartnerClick: (siteUrl: String?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PartnersViewModel = viewModel(
        factory = PartnersViewModel.Factory.create(agendaRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is PartnersUiState.Loading -> Partners(
            partners = (uiState.value as PartnersUiState.Loading).partners,
            modifier = modifier,
            isLoading = true,
            onPartnerClick = {}
        )
        is PartnersUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is PartnersUiState.Success -> Partners(
            partners = (uiState.value as PartnersUiState.Success).partners,
            modifier = modifier,
            onPartnerClick = onPartnerClick
        )
    }
}
