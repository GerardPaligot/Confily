package com.paligot.confily.speakers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.confily.models.ui.SpeakerUi
import com.paligot.confily.models.ui.findBluesky
import com.paligot.confily.models.ui.findEmail
import com.paligot.confily.models.ui.findFacebook
import com.paligot.confily.models.ui.findGitHub
import com.paligot.confily.models.ui.findInstagram
import com.paligot.confily.models.ui.findLinkedIn
import com.paligot.confily.models.ui.findMastodon
import com.paligot.confily.models.ui.findWebsite
import com.paligot.confily.models.ui.findX
import com.paligot.confily.models.ui.findYouTube
import com.paligot.confily.style.components.markdown.MarkdownText
import com.paligot.confily.style.components.placeholder.placeholder
import com.paligot.confily.style.events.socials.SocialsSection
import com.paligot.confily.style.speakers.avatar.MediumSpeakerAvatar

@Composable
fun SpeakerDetailSectionVertical(
    speaker: SpeakerUi,
    onLinkClicked: (url: String) -> Unit,
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
            hasSocials = speaker.socials.isNotEmpty(),
            xUrl = speaker.socials.findX()?.url,
            mastodonUrl = speaker.socials.findMastodon()?.url,
            blueskyUrl = speaker.socials.findBluesky()?.url,
            facebookUrl = speaker.socials.findFacebook()?.url,
            instagramUrl = speaker.socials.findInstagram()?.url,
            youtubeUrl = speaker.socials.findYouTube()?.url,
            githubUrl = speaker.socials.findGitHub()?.url,
            linkedinUrl = speaker.socials.findLinkedIn()?.url,
            websiteUrl = speaker.socials.findWebsite()?.url,
            emailUrl = speaker.socials.findEmail()?.url
        )
        Spacer(modifier = Modifier.height(8.dp))
        MarkdownText(
            text = speaker.bio,
            modifier = Modifier.placeholder(visible = isLoading)
        )
    }
}
