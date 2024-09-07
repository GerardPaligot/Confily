package com.paligot.confily.schedules.ui.talks

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun TimePreview() {
    ConfilyTheme {
        Time(time = "10:00")
    }
}
