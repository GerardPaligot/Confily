package org.gdglille.devfest.android.theme.m3.partners.feature

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.repositories.AgendaRepository

@Composable
fun PartnersVM(
    agendaRepository: AgendaRepository,
    onPartnerClick: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PartnersViewModel = viewModel(
        factory = PartnersViewModel.Factory.create(agendaRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is PartnersUiState.Loading -> PartnersOrientable(
            partners = (uiState.value as PartnersUiState.Loading).partners,
            modifier = modifier,
            isLoading = true,
            onPartnerClick = {}
        )

        is PartnersUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is PartnersUiState.Success -> PartnersOrientable(
            partners = (uiState.value as PartnersUiState.Success).partners,
            modifier = modifier,
            onPartnerClick = onPartnerClick
        )
    }
}
