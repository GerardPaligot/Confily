package org.gdglille.devfest.theme.m3.schedules.ui.talks

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.Conferences4HallTheme

@Preview
@Composable
private fun TimePreview() {
    Conferences4HallTheme {
        Time(time = "10:00")
    }
}
