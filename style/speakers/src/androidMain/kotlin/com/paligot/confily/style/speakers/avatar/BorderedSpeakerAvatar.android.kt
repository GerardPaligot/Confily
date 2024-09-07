package com.paligot.confily.style.speakers.avatar

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun SmallBorderedSpeakerAvatarPreview() {
    ConfilyTheme {
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
    ConfilyTheme {
        Column {
            MediumBorderedSpeakerAvatar(
                url = "",
                contentDescription = null
            )
        }
    }
}
