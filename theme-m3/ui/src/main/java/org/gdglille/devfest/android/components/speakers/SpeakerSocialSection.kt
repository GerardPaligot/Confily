package org.gdglille.devfest.android.components.speakers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.components.structure.SocialsSection
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.models.SpeakerUi

@Composable
fun SpeakerSocialSection(
    speaker: SpeakerUi,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    SocialsSection(
        title = speaker.name,
        subtitle = speaker.company,
        detailed = speaker.bio,
        modifier = modifier,
        isLoading = isLoading,
        twitterUrl = speaker.twitterUrl,
        githubUrl = speaker.githubUrl,
        onLinkClicked = onLinkClicked
    )
}

@Preview
@Composable
fun SpeakerSocialSectionPreview() {
    Conferences4HallTheme {
        SpeakerSocialSection(
            speaker = SpeakerUi.fake,
            onLinkClicked = {}
        )
    }
}
