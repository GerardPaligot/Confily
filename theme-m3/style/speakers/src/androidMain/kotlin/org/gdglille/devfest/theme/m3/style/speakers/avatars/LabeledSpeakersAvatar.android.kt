package org.gdglille.devfest.theme.m3.style.speakers.avatars

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme

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
