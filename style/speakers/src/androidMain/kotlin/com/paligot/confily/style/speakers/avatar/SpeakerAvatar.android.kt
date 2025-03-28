package com.paligot.confily.style.speakers.avatar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun MediumSpeakerAvatarPreview() {
    ConfilyTheme {
        Column {
            MediumSpeakerAvatar(
                url = "",
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun LargeSpeakerAvatarPreview() {
    ConfilyTheme {
        Column {
            LargeSpeakerAvatar(
                url = "",
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .aspectRatio(1f)
            )
        }
    }
}
