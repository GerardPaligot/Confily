package org.gdglille.devfest.theme.m3.speakers.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.models.ui.SpeakerUi

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun SpeakerDetailSectionVerticalPreview() {
    Conferences4HallTheme {
        SpeakerDetailSectionVertical(
            speaker = SpeakerUi.fake,
            onLinkClicked = {}
        )
    }
}
