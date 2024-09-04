package com.paligot.confily.style.partners.jobs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.Conferences4HallTheme

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun JobItemPreview() {
    Conferences4HallTheme {
        JobItem(
            title = "Mobile Staff Engineer",
            description = "Google - Paris, France",
            requirements = 5,
            propulsedBy = "WeLoveDevs",
            salaryMin = 55,
            salaryMax = 75,
            salaryRecurrence = "year",
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
