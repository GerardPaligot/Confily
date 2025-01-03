package com.paligot.confily.speakers.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.speakers.ui.models.SpeakerInfoUi
import com.paligot.confily.style.theme.ConfilyTheme

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun SpeakerDetailSectionVerticalPreview() {
    ConfilyTheme {
        SpeakerDetailSectionVertical(
            info = SpeakerInfoUi.fake,
            onLinkClicked = {}
        )
    }
}
