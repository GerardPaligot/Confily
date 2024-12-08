package com.paligot.confily.partners.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.confily.models.ui.PartnerUi
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
import com.paligot.confily.style.partners.items.PartnerItem

@Composable
fun PartnerDetailSectionVertical(
    partnerUi: PartnerUi,
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
            PartnerItem(
                url = partnerUi.logoUrl,
                contentDescription = null,
                onClick = {},
                modifier = Modifier.size(128.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        SocialsSection(
            title = partnerUi.name,
            pronouns = null,
            subtitle = null,
            onLinkClicked = onLinkClicked,
            isLoading = isLoading,
            hasSocials = partnerUi.socials.isNotEmpty(),
            xUrl = partnerUi.socials.findX()?.url,
            mastodonUrl = partnerUi.socials.findMastodon()?.url,
            blueskyUrl = partnerUi.socials.findBluesky()?.url,
            facebookUrl = partnerUi.socials.findFacebook()?.url,
            instagramUrl = partnerUi.socials.findInstagram()?.url,
            youtubeUrl = partnerUi.socials.findYouTube()?.url,
            githubUrl = partnerUi.socials.findGitHub()?.url,
            linkedinUrl = partnerUi.socials.findLinkedIn()?.url,
            websiteUrl = partnerUi.socials.findWebsite()?.url,
            emailUrl = partnerUi.socials.findEmail()?.url
        )
        Spacer(modifier = Modifier.height(8.dp))
        MarkdownText(
            text = partnerUi.description,
            modifier = Modifier.placeholder(visible = isLoading)
        )
    }
}
