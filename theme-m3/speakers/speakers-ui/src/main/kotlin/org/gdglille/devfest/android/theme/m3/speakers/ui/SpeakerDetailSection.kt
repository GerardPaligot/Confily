package org.gdglille.devfest.android.theme.m3.speakers.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.events.socials.SocialsSection
import org.gdglille.devfest.android.theme.m3.style.markdown.MarkdownText
import org.gdglille.devfest.android.theme.m3.style.placeholder.placeholder
import org.gdglille.devfest.android.theme.m3.style.speakers.avatar.MediumSpeakerAvatar
import org.gdglille.devfest.models.ui.SpeakerUi

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SpeakerDetailSectionVertical(
    speaker: SpeakerUi,
    onLinkClicked: (url: String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    displayAvatar: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if (displayAvatar) {
            MediumSpeakerAvatar(
                url = speaker.url,
                contentDescription = null,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                modifier = Modifier.placeholder(visible = isLoading)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        SocialsSection(
            title = speaker.name,
            pronouns = speaker.pronouns,
            subtitle = speaker.activity,
            onLinkClicked = onLinkClicked,
            isLoading = isLoading,
            twitterUrl = speaker.twitterUrl,
            mastodonUrl = speaker.mastodonUrl,
            githubUrl = speaker.githubUrl,
            linkedinUrl = speaker.linkedinUrl,
            websiteUrl = speaker.websiteUrl
        )
        Spacer(modifier = Modifier.height(8.dp))
        MarkdownText(
            text = speaker.bio,
            modifier = Modifier.placeholder(visible = isLoading)
        )
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalSharedTransitionApi::class)
@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun SpeakerDetailSectionVerticalPreview() {
    Conferences4HallTheme {
        SharedTransitionLayout {
            AnimatedContent(targetState = "", label = "") {
                SpeakerDetailSectionVertical(
                    speaker = SpeakerUi.fake,
                    onLinkClicked = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@AnimatedContent
                )
            }
        }
    }
}
