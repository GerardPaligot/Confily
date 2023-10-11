package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.theme.m3.partners.feature.PartnersUiState
import org.gdglille.devfest.android.theme.m3.partners.feature.PartnersViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.screens.partners.Partners
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.AgendaRepository

@Composable
fun PartnersVM(
    agendaRepository: AgendaRepository,
    onPartnerClick: (siteUrl: String?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: org.gdglille.devfest.android.theme.m3.partners.feature.PartnersViewModel = viewModel(
        factory = org.gdglille.devfest.android.theme.m3.partners.feature.PartnersViewModel.Factory.create(agendaRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is org.gdglille.devfest.android.theme.m3.partners.feature.PartnersUiState.Loading -> Partners(
            partners = (uiState.value as org.gdglille.devfest.android.theme.m3.partners.feature.PartnersUiState.Loading).partners,
            modifier = modifier,
            isLoading = true,
            onPartnerClick = {}
        )
        is org.gdglille.devfest.android.theme.m3.partners.feature.PartnersUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is org.gdglille.devfest.android.theme.m3.partners.feature.PartnersUiState.Success -> Partners(
            partners = (uiState.value as org.gdglille.devfest.android.theme.m3.partners.feature.PartnersUiState.Success).partners,
            modifier = modifier,
            onPartnerClick = onPartnerClick
        )
    }
}
