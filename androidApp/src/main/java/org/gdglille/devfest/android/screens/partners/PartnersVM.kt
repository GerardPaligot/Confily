package org.gdglille.devfest.android.screens.partners

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.R
import org.gdglille.devfest.android.screens.Partners
import org.gdglille.devfest.repositories.AgendaRepository

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
