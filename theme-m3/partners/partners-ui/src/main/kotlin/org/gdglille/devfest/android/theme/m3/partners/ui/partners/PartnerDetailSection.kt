package org.gdglille.devfest.android.theme.m3.partners.ui.partners

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.markdowns.MarkdownText
import org.gdglille.devfest.android.theme.m3.style.partners.PartnerItem
import org.gdglille.devfest.android.theme.m3.style.placeholder
import org.gdglille.devfest.android.theme.m3.style.previews.ThemedPreviews
import org.gdglille.devfest.android.theme.m3.style.socials.SocialsSection
import org.gdglille.devfest.models.ui.PartnerItemUi

@Composable
fun PartnerDetailSectionVertical(
    partnerItemUi: PartnerItemUi,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    displayAvatar: Boolean = true,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if (displayAvatar) {
            PartnerItem(
                url = partnerItemUi.logoUrl,
                contentDescription = null,
                onClick = {},
                modifier = Modifier.size(128.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        SocialsSection(
            title = partnerItemUi.name,
            pronouns = null,
            subtitle = null,
            isLoading = isLoading,
            twitterUrl = partnerItemUi.twitterUrl,
            githubUrl = null,
            linkedinUrl = partnerItemUi.linkedinUrl,
            websiteUrl = partnerItemUi.siteUrl,
            onLinkClicked = onLinkClicked
        )
        Spacer(modifier = Modifier.height(8.dp))
        MarkdownText(
            text = partnerItemUi.description,
            modifier = Modifier.placeholder(visible = isLoading)
        )
    }
}

@Suppress("UnusedPrivateMember")
@ThemedPreviews
@Composable
private fun PartnerDetailSectionVerticalPreview() {
    Conferences4HallTheme {
        PartnerDetailSectionVertical(
            partnerItemUi = PartnerItemUi.fake,
            onLinkClicked = {}
        )
    }
}
