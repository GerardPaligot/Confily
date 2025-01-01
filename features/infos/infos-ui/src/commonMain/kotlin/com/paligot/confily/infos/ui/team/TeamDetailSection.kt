package com.paligot.confily.infos.ui.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.confily.models.ui.TeamMemberUi
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
fun TeamDetailSection(
    uiModel: TeamMemberUi,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Column(modifier = modifier) {
        if (uiModel.photoUrl != null) {
            MediumSpeakerAvatar(
                url = uiModel.photoUrl!!,
                contentDescription = null,
                modifier = Modifier.placeholder(visible = isLoading)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        SocialsSection(
            title = uiModel.displayName,
            pronouns = null,
            subtitle = uiModel.role,
            onLinkClicked = onLinkClicked,
            isLoading = isLoading,
            hasSocials = uiModel.socials.isNotEmpty(),
            xUrl = uiModel.socials.findX()?.url,
            mastodonUrl = uiModel.socials.findMastodon()?.url,
            blueskyUrl = uiModel.socials.findBluesky()?.url,
            facebookUrl = uiModel.socials.findFacebook()?.url,
            instagramUrl = uiModel.socials.findInstagram()?.url,
            youtubeUrl = uiModel.socials.findYouTube()?.url,
            githubUrl = uiModel.socials.findGitHub()?.url,
            linkedinUrl = uiModel.socials.findLinkedIn()?.url,
            websiteUrl = uiModel.socials.findWebsite()?.url,
            emailUrl = uiModel.socials.findEmail()?.url
        )
        Spacer(modifier = Modifier.height(8.dp))
        MarkdownText(
            text = uiModel.bio,
            modifier = Modifier.placeholder(visible = isLoading)
        )
    }
}
