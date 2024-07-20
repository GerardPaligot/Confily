package org.gdglille.devfest.theme.m3.schedules.ui.speakers

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.models.ui.SpeakerItemUi

@Preview
@Composable
private fun SpeakerItemRowPreview() {
    Conferences4HallTheme {
        SpeakerItemRow(
            speakers = persistentListOf(SpeakerItemUi.fake, SpeakerItemUi.fake),
            onSpeakerItemClick = {}
        )
    }
}
