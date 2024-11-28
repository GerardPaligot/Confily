package com.paligot.confily.partners.panes

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.PartnerUi
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun PartnerDetailVerticalScreenPreview() {
    ConfilyTheme {
        Scaffold {
            PartnerDetailVerticalScreen(
                partnerUi = PartnerUi.fake,
                onLinkClicked = {},
                onItineraryClicked = { _, _ -> },
                contentPadding = it
            )
        }
    }
}
