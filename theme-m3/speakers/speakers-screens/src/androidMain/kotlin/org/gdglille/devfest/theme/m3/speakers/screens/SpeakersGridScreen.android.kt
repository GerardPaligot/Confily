package org.gdglille.devfest.theme.m3.speakers.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.SpeakerItemUi
import com.paligot.confily.style.theme.Conferences4HallTheme
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
private fun SpeakersGridPreview() {
    Conferences4HallTheme {
        SpeakersGridScreen(
            speakers = persistentListOf(
                SpeakerItemUi.fake.copy(id = "1"),
                SpeakerItemUi.fake.copy(id = "2"),
                SpeakerItemUi.fake.copy(id = "3"),
                SpeakerItemUi.fake.copy(id = "4")
            ),
            onSpeakerClicked = {}
        )
    }
}
