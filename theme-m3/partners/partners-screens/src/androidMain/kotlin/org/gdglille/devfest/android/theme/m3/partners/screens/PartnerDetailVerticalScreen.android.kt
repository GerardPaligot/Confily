package org.gdglille.devfest.android.theme.m3.partners.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.PartnerItemUi
import com.paligot.confily.style.theme.Conferences4HallTheme
import org.gdglille.devfest.theme.m3.partners.screens.PartnerDetailVerticalScreen

@Preview
@Composable
private fun PartnerDetailVerticalScreenPreview() {
    Conferences4HallTheme {
        Scaffold {
            PartnerDetailVerticalScreen(
                partnerItemUi = PartnerItemUi.fake,
                onLinkClicked = {},
                onItineraryClicked = { _, _ -> },
                contentPadding = it
            )
        }
    }
}
