package com.paligot.confily.style.partners.activities

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun SmallActivityItemPreview() {
    ConfilyTheme {
        SmallActivityItem(
            activityName = "Kahoot",
            partnerName = "WeLoveDevs",
            time = "08h00"
        )
    }
}

@Preview
@Composable
private fun MediumActivityItemPreview() {
    ConfilyTheme {
        MediumActivityItem(
            activityName = "Kahoot",
            partnerName = "WeLoveDevs",
            time = "08h00"
        )
    }
}
