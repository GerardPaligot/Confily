package org.gdglille.devfest.android.theme.vitamin.ui.components.speakers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.vitamin.ui.components.structure.SocialsSection
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.models.SpeakerUi

@Composable
fun SpeakerSocialSection(
    speaker: SpeakerUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onLinkClicked: (url: String) -> Unit,
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
            speaker = SpeakerUi.fake
        ) {}
    }
}
