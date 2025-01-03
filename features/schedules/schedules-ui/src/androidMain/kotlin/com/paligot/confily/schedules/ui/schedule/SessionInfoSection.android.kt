package com.paligot.confily.schedules.ui.schedule

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.schedules.ui.models.SessionInfoUi
import com.paligot.confily.schedules.ui.models.SpeakerItemUi
import com.paligot.confily.style.theme.ConfilyTheme
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
private fun SessionInfoSectionPreview() {
    ConfilyTheme {
        SessionInfoSection(
            info = SessionInfoUi.fake,
            speakers = persistentListOf(SpeakerItemUi.fake)
        )
    }
}
