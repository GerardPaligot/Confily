package com.paligot.confily.style.speakers.avatar

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.Conferences4HallTheme

@Preview
@Composable
private fun SmallBorderedSpeakerAvatarPreview() {
    Conferences4HallTheme {
        Column {
            SmallBorderedSpeakerAvatar(
                url = "",
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun MediumBorderedSpeakerAvatarPreview() {
    Conferences4HallTheme {
        Column {
            MediumBorderedSpeakerAvatar(
                url = "",
                contentDescription = null
            )
        }
    }
}
