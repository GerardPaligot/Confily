package org.gdglille.devfest.theme.m3.style.speakers.avatars

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.Conferences4HallTheme
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
private fun SmallBorderedSpeakersAvatarPreview() {
    Conferences4HallTheme {
        SmallBorderedSpeakersAvatar(
            urls = persistentListOf("", ""),
            descriptions = persistentListOf("", "")
        )
    }
}

@Preview
@Composable
private fun MediumBorderedSpeakersAvatarPreview() {
    Conferences4HallTheme {
        MediumBorderedSpeakersAvatar(
            urls = persistentListOf("", ""),
            descriptions = persistentListOf("", "")
        )
    }
}
