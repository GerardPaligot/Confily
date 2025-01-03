package com.paligot.confily.speakers.panes

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.speakers.panes.models.SpeakerUi
import com.paligot.confily.style.theme.ConfilyTheme

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun SpeakerDetailScreenPreview() {
    ConfilyTheme {
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
