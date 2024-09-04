package com.paligot.confily.speakers.panes

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.SpeakerUi
import com.paligot.confily.style.theme.Conferences4HallTheme

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun SpeakerDetailScreenPreview() {
    Conferences4HallTheme {
        Scaffold {
            SpeakerDetailScreen(
                speaker = SpeakerUi.fake,
                onTalkClicked = {},
                onFavoriteClicked = {},
                onLinkClicked = {},
                contentPadding = it
            )
        }
    }
}
