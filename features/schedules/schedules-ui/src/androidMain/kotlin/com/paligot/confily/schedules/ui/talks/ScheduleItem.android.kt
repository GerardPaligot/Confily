package com.paligot.confily.schedules.ui.talks

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.style.theme.ConfilyTheme

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun SmallTalkItemPreview() {
    ConfilyTheme {
        SmallScheduleItem(talk = TalkItemUi.fake) { }
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun MediumTalkItemPreview() {
    ConfilyTheme {
        MediumScheduleItem(talk = TalkItemUi.fake) { }
    }
}
