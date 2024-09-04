package org.gdglille.devfest.theme.m3.schedules.ui.talks

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.style.theme.Conferences4HallTheme

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun SmallTalkItemPreview() {
    Conferences4HallTheme {
        SmallScheduleItem(talk = TalkItemUi.fake) { }
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun MediumTalkItemPreview() {
    Conferences4HallTheme {
        MediumScheduleItem(talk = TalkItemUi.fake) { }
    }
}
