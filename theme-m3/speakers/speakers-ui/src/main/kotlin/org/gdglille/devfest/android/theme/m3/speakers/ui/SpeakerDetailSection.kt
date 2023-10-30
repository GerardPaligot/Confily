package org.gdglille.devfest.android.theme.m3.speakers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.placeholder
import org.gdglille.devfest.android.theme.m3.style.socials.SocialsSection
import org.gdglille.devfest.android.theme.m3.style.speakers.avatar.MediumSpeakerAvatar
import org.gdglille.devfest.models.ui.SpeakerUi

@Composable
fun SpeakerDetailSection(
    speaker: SpeakerUi,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        MediumSpeakerAvatar(
            url = speaker.url,
            contentDescription = null,
            modifier = Modifier.placeholder(visible = isLoading)
        )
        Spacer(modifier = Modifier.height(16.dp))
        SocialsSection(
            title = speaker.name,
            pronouns = speaker.pronouns,
            subtitle = speaker.activity,
            detailed = speaker.bio,
            isLoading = isLoading,
            twitterUrl = speaker.twitterUrl,
            mastodonUrl = speaker.mastodonUrl,
            githubUrl = speaker.githubUrl,
            linkedinUrl = speaker.linkedinUrl,
            websiteUrl = speaker.websiteUrl,
            onLinkClicked = onLinkClicked
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview
@Composable
private fun SpeakerDetailSectionPreview() {
    Conferences4HallTheme {
        SpeakerDetailSection(
            speaker = SpeakerUi.fake,
            onLinkClicked = {}
        )
    }
}
