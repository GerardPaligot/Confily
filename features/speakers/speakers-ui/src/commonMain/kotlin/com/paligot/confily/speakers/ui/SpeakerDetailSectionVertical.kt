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
import com.paligot.confily.socials.ui.SocialsSection
import com.paligot.confily.speakers.ui.models.SpeakerInfoUi
import com.paligot.confily.style.components.markdown.MarkdownText
import com.paligot.confily.style.components.placeholder.placeholder
import com.paligot.confily.style.speakers.avatar.MediumSpeakerAvatar

@Composable
fun SpeakerDetailSectionVertical(
    info: SpeakerInfoUi,
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
                url = info.url,
                contentDescription = null,
                modifier = Modifier.placeholder(visible = isLoading)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        SocialsSection(
            title = info.name,
            pronouns = info.pronouns,
            subtitle = info.activity,
            onLinkClicked = onLinkClicked,
            isLoading = isLoading,
            socials = info.socials
        )
        Spacer(modifier = Modifier.height(8.dp))
        MarkdownText(
            text = info.bio,
            modifier = Modifier.placeholder(visible = isLoading)
        )
    }
}
