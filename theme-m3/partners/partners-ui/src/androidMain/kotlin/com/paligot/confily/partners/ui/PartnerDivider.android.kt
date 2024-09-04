package com.paligot.confily.partners.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.Conferences4HallTheme

@Preview
@Composable
private fun PartnerDividerPreview() {
    Conferences4HallTheme {
        PartnerDivider(title = "Gold")
    }
}
