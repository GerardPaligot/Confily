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
import com.paligot.confily.models.ui.SocialTypeUi
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
            xUrl = partnerUi.socials.find { it.type == SocialTypeUi.X }?.url,
            mastodonUrl = partnerUi.socials.find { it.type == SocialTypeUi.Mastodon }?.url,
            blueskyUrl = partnerUi.socials.find { it.type == SocialTypeUi.Bluesky }?.url,
            facebookUrl = partnerUi.socials.find { it.type == SocialTypeUi.Facebook }?.url,
            instagramUrl = partnerUi.socials.find { it.type == SocialTypeUi.Instagram }?.url,
            youtubeUrl = partnerUi.socials.find { it.type == SocialTypeUi.YouTube }?.url,
            githubUrl = partnerUi.socials.find { it.type == SocialTypeUi.GitHub }?.url,
            linkedinUrl = partnerUi.socials.find { it.type == SocialTypeUi.LinkedIn }?.url,
            websiteUrl = partnerUi.socials.find { it.type == SocialTypeUi.Website }?.url,
            emailUrl = partnerUi.socials.find { it.type == SocialTypeUi.Email }?.url
        )
        Spacer(modifier = Modifier.height(8.dp))
        MarkdownText(
            text = partnerUi.description,
            modifier = Modifier.placeholder(visible = isLoading)
        )
    }
}
