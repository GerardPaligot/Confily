package com.paligot.confily.partners.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.partners.panes.PartnerDetailOrientable
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import com.paligot.confily.style.theme.appbars.AppBarIcons
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PartnerDetailVM(
    partnerId: String,
    onLinkClicked: (url: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (AppBarIcons.() -> Unit)? = null,
    isLandscape: Boolean = false,
    viewModel: PartnerDetailViewModel = koinViewModel(
        key = partnerId,
        parameters = { parametersOf(partnerId) }
    )
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is PartnerUiState.Loading -> PartnerDetailOrientable(
            partnerItemUi = uiState.partner,
            onLinkClicked = {},
            onItineraryClicked = { _, _ -> },
            modifier = modifier,
            navigationIcon = navigationIcon,
            isLandscape = isLandscape,
            isLoading = true
        )

        is PartnerUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is PartnerUiState.Success -> PartnerDetailOrientable(
            partnerItemUi = uiState.partner,
            onLinkClicked = onLinkClicked,
            onItineraryClicked = onItineraryClicked,
            modifier = modifier,
            navigationIcon = navigationIcon,
            isLandscape = isLandscape
        )
    }
}
