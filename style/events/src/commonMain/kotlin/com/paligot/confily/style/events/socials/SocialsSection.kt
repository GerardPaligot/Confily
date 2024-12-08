package com.paligot.confily.style.events.socials

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.paligot.confily.style.components.placeholder.placeholder
import com.paligot.confily.style.theme.toDp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SocialsSection(
    title: String,
    pronouns: String?,
    subtitle: String?,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    hasSocials: Boolean = false,
    xUrl: String? = null,
    mastodonUrl: String? = null,
    blueskyUrl: String? = null,
    facebookUrl: String? = null,
    instagramUrl: String? = null,
    youtubeUrl: String? = null,
    githubUrl: String? = null,
    linkedinUrl: String? = null,
    websiteUrl: String? = null,
    emailUrl: String? = null,
    titleColor: Color = SocialsSectionDefaults.titleColor,
    pronounsColor: Color = SocialsSectionDefaults.pronounsColor,
    subTitleColor: Color = SocialsSectionDefaults.subTitleColor,
    titleTextStyle: TextStyle = SocialsSectionDefaults.titleTextStyle,
    pronounsTextStyle: TextStyle = SocialsSectionDefaults.pronounsTextStyle,
    subTitleTextStyle: TextStyle = SocialsSectionDefaults.subTitleTextStyle
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = titleTextStyle,
            color = titleColor,
            modifier = Modifier.placeholder(visible = isLoading)
        )
        pronouns?.let {
            Text(
                text = pronouns,
                style = pronounsTextStyle,
                color = pronounsColor,
                modifier = Modifier.placeholder(visible = isLoading)
            )
        }
        subtitle?.let {
            Spacer(
                modifier = Modifier
                    .height(SocialsSectionTokens.BetweenTitleAndSubTitleSpacing.toDp())
            )
            Text(
                text = subtitle,
                style = subTitleTextStyle,
                color = subTitleColor,
                modifier = Modifier.placeholder(visible = isLoading)
            )
        }
        Spacer(modifier = Modifier.height(SocialsSectionTokens.BetweenTitleAndSocialsSpacing.toDp()))
        if (hasSocials) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(
                    SocialsSectionTokens.BetweenSocialIconsSpacing.toDp()
                ),
                verticalArrangement = Arrangement.spacedBy(
                    SocialsSectionTokens.BetweenSocialIconsSpacing.toDp()
                )
            ) {
                xUrl?.let {
                    SocialIcons.X(
                        text = title,
                        onClick = { xUrl.let(onLinkClicked) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
                mastodonUrl?.let {
                    SocialIcons.Mastodon(
                        text = title,
                        onClick = { mastodonUrl.let(onLinkClicked) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
                blueskyUrl?.let {
                    SocialIcons.Bluesky(
                        text = title,
                        onClick = { blueskyUrl.let(onLinkClicked) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
                facebookUrl?.let {
                    SocialIcons.Facebook(
                        text = title,
                        onClick = { facebookUrl.let(onLinkClicked) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
                instagramUrl?.let {
                    SocialIcons.Instagram(
                        text = title,
                        onClick = { instagramUrl.let(onLinkClicked) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
                youtubeUrl?.let {
                    SocialIcons.YouTube(
                        text = title,
                        onClick = { youtubeUrl.let(onLinkClicked) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
                githubUrl?.let {
                    SocialIcons.GitHub(
                        text = title,
                        onClick = { githubUrl.let(onLinkClicked) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
                linkedinUrl?.let {
                    SocialIcons.LinkedIn(
                        text = title,
                        onClick = { linkedinUrl.let(onLinkClicked) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
                websiteUrl?.let {
                    SocialIcons.Website(
                        text = title,
                        onClick = { websiteUrl.let(onLinkClicked) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
                emailUrl?.let {
                    SocialIcons.Email(
                        text = title,
                        onClick = { emailUrl.let(onLinkClicked) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
            }
        }
    }
}
