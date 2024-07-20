package org.gdglille.devfest.theme.m3.schedules.ui.talks

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.models.ui.TalkItemUi

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
