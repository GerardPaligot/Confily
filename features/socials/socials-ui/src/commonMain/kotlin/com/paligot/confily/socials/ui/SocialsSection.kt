package com.paligot.confily.socials.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.paligot.confily.socials.ui.models.SocialUi
import com.paligot.confily.socials.ui.models.findBluesky
import com.paligot.confily.socials.ui.models.findEmail
import com.paligot.confily.socials.ui.models.findFacebook
import com.paligot.confily.socials.ui.models.findGitHub
import com.paligot.confily.socials.ui.models.findInstagram
import com.paligot.confily.socials.ui.models.findLinkedIn
import com.paligot.confily.socials.ui.models.findMastodon
import com.paligot.confily.socials.ui.models.findWebsite
import com.paligot.confily.socials.ui.models.findX
import com.paligot.confily.socials.ui.models.findYouTube
import com.paligot.confily.style.events.socials.SocialsSectionDefaults
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SocialsSection(
    title: String,
    pronouns: String?,
    subtitle: String?,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    socials: ImmutableList<SocialUi>,
    titleColor: Color = SocialsSectionDefaults.titleColor,
    pronounsColor: Color = SocialsSectionDefaults.pronounsColor,
    subTitleColor: Color = SocialsSectionDefaults.subTitleColor,
    titleTextStyle: TextStyle = SocialsSectionDefaults.titleTextStyle,
    pronounsTextStyle: TextStyle = SocialsSectionDefaults.pronounsTextStyle,
    subTitleTextStyle: TextStyle = SocialsSectionDefaults.subTitleTextStyle
) {
    com.paligot.confily.style.events.socials.SocialsSection(
        title = title,
        pronouns = pronouns,
        subtitle = subtitle,
        onLinkClicked = onLinkClicked,
        modifier = modifier,
        isLoading = isLoading,
        hasSocials = socials.isNotEmpty(),
        xUrl = socials.findX()?.url,
        mastodonUrl = socials.findMastodon()?.url,
        blueskyUrl = socials.findBluesky()?.url,
        facebookUrl = socials.findFacebook()?.url,
        instagramUrl = socials.findInstagram()?.url,
        youtubeUrl = socials.findYouTube()?.url,
        githubUrl = socials.findGitHub()?.url,
        linkedinUrl = socials.findLinkedIn()?.url,
        websiteUrl = socials.findWebsite()?.url,
        emailUrl = socials.findEmail()?.url,
        titleColor = titleColor,
        pronounsColor = pronounsColor,
        subTitleColor = subTitleColor,
        titleTextStyle = titleTextStyle,
        pronounsTextStyle = pronounsTextStyle,
        subTitleTextStyle = subTitleTextStyle
    )
}
