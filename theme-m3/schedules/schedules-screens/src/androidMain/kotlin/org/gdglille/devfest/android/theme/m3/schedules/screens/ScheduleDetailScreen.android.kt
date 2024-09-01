package org.gdglille.devfest.android.theme.m3.schedules.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.models.ui.TalkUi

@ExperimentalMaterial3Api
@Preview
@Composable
private fun ScheduleDetailPreview() {
    Conferences4HallTheme {
        Surface {
            ScheduleDetailScreen(
                talk = TalkUi.fake,
                onSpeakerClicked = {}
            )
        }
    }
}
