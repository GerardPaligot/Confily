package org.gdglille.devfest.theme.m3.style.speakers.avatars

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.Conferences4HallTheme
import kotlinx.collections.immutable.persistentListOf

@Preview(showBackground = true)
@Composable
private fun HorizontalSpeakersListPreview() {
    Conferences4HallTheme {
        SmallLabeledSpeakersAvatar(
            label = "John Doe and Janne Doe",
            urls = persistentListOf("", "")
        )
    }
}
