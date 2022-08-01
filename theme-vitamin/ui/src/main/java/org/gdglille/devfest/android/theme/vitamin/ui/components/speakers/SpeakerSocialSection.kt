package org.gdglille.devfest.android.theme.vitamin.ui.components.speakers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.models.SpeakerUi

@Composable
fun SpeakerSocialSection(
    speaker: SpeakerUi,
    modifier: Modifier = Modifier,
    onTwitterClick: (url: String) -> Unit,
    onGitHubClick: (url: String) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = speaker.name,
            style = VitaminTheme.typography.h4,
            color = VitaminTheme.colors.vtmnContentPrimary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = speaker.company,
            style = VitaminTheme.typography.body1,
            color = VitaminTheme.colors.vtmnContentSecondary
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (speaker.twitter != null || speaker.github != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                speaker.twitter?.let {
                    Socials.Twitter(
                        text = it,
                        onClick = { speaker.twitterUrl?.let(onTwitterClick) }
                    )
                }
                speaker.github?.let {
                    Socials.GitHub(
                        text = it,
                        onClick = { speaker.githubUrl?.let(onGitHubClick) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SpeakerSocialSectionPreview() {
    Conferences4HallTheme {
        SpeakerSocialSection(
            speaker = SpeakerUi.fake,
            onTwitterClick = {},
            onGitHubClick = {}
        )
    }
}
