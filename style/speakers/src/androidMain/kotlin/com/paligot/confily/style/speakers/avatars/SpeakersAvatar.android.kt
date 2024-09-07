package com.paligot.confily.style.speakers.avatars

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.ConfilyTheme
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
private fun SmallBorderedSpeakersAvatarPreview() {
    ConfilyTheme {
        SmallBorderedSpeakersAvatar(
            urls = persistentListOf("", ""),
            descriptions = persistentListOf("", "")
        )
    }
}

@Preview
@Composable
private fun MediumBorderedSpeakersAvatarPreview() {
    ConfilyTheme {
        MediumBorderedSpeakersAvatar(
            urls = persistentListOf("", ""),
            descriptions = persistentListOf("", "")
        )
    }
}
