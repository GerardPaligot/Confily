package org.gdglille.devfest.android.theme.m3.partners.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.models.ui.PartnerItemUi

@Composable
fun PartnerDetailOrientable(
    partnerItemUi: PartnerItemUi,
    onLinkClicked: (url: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isLoading: Boolean = false,
) {
    val state = rememberLazyListState()
    val orientation = LocalConfiguration.current
    if (orientation.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = modifier.padding(contentPadding)
        ) {
            PartnerImageScreen(
                url = partnerItemUi.logoUrl,
                isLoading = isLoading,
                modifier = Modifier.weight(1f)
            )
            PartnerDetailVerticalScreen(
                partnerItemUi = partnerItemUi,
                onLinkClicked = onLinkClicked,
                onItineraryClicked = onItineraryClicked,
                modifier = Modifier.weight(1f),
                state = state,
                isLoading = isLoading,
                displayAvatar = false
            )
        }
    } else {
        PartnerDetailVerticalScreen(
            partnerItemUi = partnerItemUi,
            onLinkClicked = onLinkClicked,
            onItineraryClicked = onItineraryClicked,
            contentPadding = contentPadding,
            state = state,
            isLoading = isLoading
        )
    }
}
