package org.gdglille.devfest.theme.m3.style.speakers.avatar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme

@Preview
@Composable
private fun MediumSpeakerAvatarPreview() {
    Conferences4HallTheme {
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
    Conferences4HallTheme {
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
