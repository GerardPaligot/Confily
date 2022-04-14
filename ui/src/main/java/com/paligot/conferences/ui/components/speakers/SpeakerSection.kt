package com.paligot.conferences.ui.components.speakers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.conferences.models.SpeakerUi
import com.paligot.conferences.ui.theme.Conferences4HallTheme
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun SpeakerSection(
    speaker: SpeakerUi,
    modifier: Modifier = Modifier,
    bodyTextStyle: TextStyle = MaterialTheme.typography.body2,
    color: Color = MaterialTheme.colors.onBackground,
    onTwitterClick: (url: String) -> Unit,
    onGitHubClick: (url: String) -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
        MarkdownText(
            markdown = speaker.bio,
            color = color,
            style = bodyTextStyle
        )
    }
}

@Preview
@Composable
fun SpeakerSectionPreview() {
    Conferences4HallTheme {
        SpeakerSection(
            speaker = SpeakerUi.fake,
            onTwitterClick = {},
            onGitHubClick = {}
        )
    }
}
