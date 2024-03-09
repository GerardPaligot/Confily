package org.gdglille.devfest.android.theme.m3.style.socials

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
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.placeholder
import org.gdglille.devfest.android.theme.m3.style.toDp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SocialsSection(
    title: String,
    pronouns: String?,
    subtitle: String?,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    twitterUrl: String? = null,
    mastodonUrl: String? = null,
    githubUrl: String? = null,
    linkedinUrl: String? = null,
    websiteUrl: String? = null,
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
        val hasUrls =
            twitterUrl != null || githubUrl != null || linkedinUrl != null || websiteUrl != null
        if (hasUrls) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(
                    SocialsSectionTokens.BetweenSocialIconsSpacing.toDp()
                ),
                verticalArrangement = Arrangement.spacedBy(
                    SocialsSectionTokens.BetweenSocialIconsSpacing.toDp()
                )
            ) {
                twitterUrl?.let {
                    SocialIcons.Twitter(
                        text = title,
                        onClick = { twitterUrl.let(onLinkClicked) },
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
            }
        }
    }
}

@Preview
@Composable
internal fun SocialsSectionPreview() {
    Conferences4HallTheme {
        SocialsSection(
            title = "Gérard Paligot",
            pronouns = "Him/He",
            subtitle = null,
            onLinkClicked = {},
            twitterUrl = "",
            githubUrl = "",
            linkedinUrl = "",
            websiteUrl = ""
        )
    }
}
