package org.gdglille.devfest.android.components.speakers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextThemeIntegration
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.models.SpeakerUi

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
        RichTextThemeIntegration(
            textStyle = { bodyTextStyle },
            ProvideTextStyle = null,
            contentColor = { color },
            ProvideContentColor = null,
        ) {
            RichText {
                Markdown(speaker.bio)
            }
        }
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
