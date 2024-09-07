package com.paligot.confily.style.speakers.avatars

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.ConfilyTheme
import kotlinx.collections.immutable.persistentListOf

@Preview(showBackground = true)
@Composable
private fun HorizontalSpeakersListPreview() {
    ConfilyTheme {
        SmallLabeledSpeakersAvatar(
            label = "John Doe and Janne Doe",
            urls = persistentListOf("", "")
        )
    }
}
