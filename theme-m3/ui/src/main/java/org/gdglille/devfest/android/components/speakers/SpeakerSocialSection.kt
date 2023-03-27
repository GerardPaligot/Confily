package org.gdglille.devfest.android.components.speakers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.components.structure.SocialsSection
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.SpeakerUi

@Composable
fun SpeakerSocialSection(
    speaker: SpeakerUi,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    val activity = if (speaker.jobTitle != null && speaker.company != null) {
        stringResource(id = R.string.text_speaker_activity, speaker.jobTitle!!, speaker.company!!)
    } else if (speaker.jobTitle != null && speaker.company == null) speaker.jobTitle
    else if (speaker.jobTitle == null && speaker.company != null) speaker.company
    else null
    SocialsSection(
        title = speaker.name,
        pronouns = speaker.pronouns,
        subtitle = activity,
        detailed = speaker.bio,
        modifier = modifier,
        isLoading = isLoading,
        twitterUrl = speaker.twitterUrl,
        mastodonUrl = speaker.mastodonUrl,
        githubUrl = speaker.githubUrl,
        linkedinUrl = speaker.linkedinUrl,
        websiteUrl = speaker.websiteUrl,
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
