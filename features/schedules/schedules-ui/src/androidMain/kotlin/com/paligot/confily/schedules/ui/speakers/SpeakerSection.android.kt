package com.paligot.confily.schedules.ui.speakers

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.SpeakerItemUi
import com.paligot.confily.style.theme.ConfilyTheme
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
private fun SpeakerSectionPreview() {
    ConfilyTheme {
        SpeakerSection(
            speakers = persistentListOf(SpeakerItemUi.fake, SpeakerItemUi.fake),
            onSpeakerItemClick = {}
        )
    }
}
