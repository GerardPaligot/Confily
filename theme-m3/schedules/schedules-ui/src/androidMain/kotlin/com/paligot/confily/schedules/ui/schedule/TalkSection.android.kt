package com.paligot.confily.schedules.ui.schedule

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.TalkUi
import com.paligot.confily.style.theme.Conferences4HallTheme

@Preview
@Composable
fun TalkSectionPreview() {
    Conferences4HallTheme {
        TalkSection(talk = TalkUi.fake)
    }
}
