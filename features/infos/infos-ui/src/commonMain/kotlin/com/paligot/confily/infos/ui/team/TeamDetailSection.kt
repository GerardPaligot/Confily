package com.paligot.confily.infos.ui.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.confily.infos.ui.models.TeamMemberUi
import com.paligot.confily.socials.ui.SocialsSection
import com.paligot.confily.style.components.markdown.MarkdownText
import com.paligot.confily.style.components.placeholder.placeholder
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
            socials = uiModel.socials
        )
        Spacer(modifier = Modifier.height(8.dp))
        MarkdownText(
            text = uiModel.bio,
            modifier = Modifier.placeholder(visible = isLoading)
        )
    }
}
