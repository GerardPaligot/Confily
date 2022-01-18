package com.paligot.conferences.android.components.speakers

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
import com.paligot.conferences.android.theme.Conferences4HallTheme

data class SpeakerUi(
    val url: String,
    val name: String,
    val company: String,
    val bio: String,
    val twitter: String?,
    val twitterUrl: String?,
    val github: String?,
    val githubUrl: String?
)

val speaker = SpeakerUi(
    url = "https://pbs.twimg.com/profile_images/1465658195767136257/zdYQWsTj_400x400.jpg",
    name = "Gérard Paligot",
    company = "Decathlon",
    bio = "Father and husband // Software Staff Engineer at Decathlon // Speaker // LAUG, FRAUG, GDGLille, DevfestLille organizer // Disney Fan!",
    twitter = "GerardPaligot",
    twitterUrl = "https://twitter.com/GerardPaligot",
    github = "GerardPaligot",
    githubUrl = "https://github.com/GerardPaligot"
)

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
        Text(text = speaker.bio, style = bodyTextStyle, color = color)
    }
}

@Preview
@Composable
fun SpeakerSectionPreview() {
    Conferences4HallTheme {
        SpeakerSection(
            speaker = speaker,
            onTwitterClick = {},
            onGitHubClick = {}
        )
    }
}
