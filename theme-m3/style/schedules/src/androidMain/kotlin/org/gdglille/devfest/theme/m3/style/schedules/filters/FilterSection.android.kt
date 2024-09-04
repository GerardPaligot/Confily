package org.gdglille.devfest.theme.m3.style.schedules.filters

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.Conferences4HallTheme

@Preview
@Composable
private fun FilterSectionPreview() {
    Conferences4HallTheme {
        FilterSection(title = "Categories") {
            Text(text = "Filter content")
        }
    }
}
