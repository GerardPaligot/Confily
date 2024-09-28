package com.paligot.confily.schedules.ui.schedule

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.TalkUi
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun TalkSectionPreview() {
    ConfilyTheme {
        TalkSection(talk = TalkUi.fake)
    }
}
