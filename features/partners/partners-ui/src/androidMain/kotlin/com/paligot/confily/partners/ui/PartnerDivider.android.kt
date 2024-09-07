package com.paligot.confily.partners.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun PartnerDividerPreview() {
    ConfilyTheme {
        PartnerDivider(title = "Gold")
    }
}
