package com.paligot.confily.partners.panes

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.PartnerItemUi
import com.paligot.confily.style.theme.Conferences4HallTheme

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
