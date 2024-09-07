package com.paligot.confily.style.schedules.filters

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun FilterSectionPreview() {
    ConfilyTheme {
        FilterSection(title = "Categories") {
            Text(text = "Filter content")
        }
    }
}
