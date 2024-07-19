package org.gdglille.devfest.theme.m3.style.speakers.avatars

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme

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
