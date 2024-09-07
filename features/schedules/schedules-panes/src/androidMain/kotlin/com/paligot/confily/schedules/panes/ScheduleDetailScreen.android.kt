package com.paligot.confily.schedules.panes

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.TalkUi
import com.paligot.confily.style.theme.ConfilyTheme

@ExperimentalMaterial3Api
@Preview
@Composable
private fun ScheduleDetailPreview() {
    ConfilyTheme {
        Surface {
            ScheduleDetailScreen(
                talk = TalkUi.fake,
                onSpeakerClicked = {}
            )
        }
    }
}
