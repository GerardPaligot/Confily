package org.gdglille.devfest.android.components.structure

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextThemeIntegration
import org.gdglille.devfest.android.components.buttons.Socials
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.placeholder
import org.gdglille.devfest.models.EventUi
import org.gdglille.devfest.models.PartnerItemUi

@Composable
fun SocialsSection(
    title: String,
    pronouns: String?,
    subtitle: String?,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    detailed: String? = null,
    isLoading: Boolean = false,
    twitterUrl: String? = null,
    mastodonUrl: String? = null,
    githubUrl: String? = null,
    linkedinUrl: String? = null,
    websiteUrl: String? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.placeholder(visible = isLoading)
        )
        pronouns?.let {
            Text(
                text = pronouns,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .5f),
                modifier = Modifier.placeholder(visible = isLoading)
            )
        }
        subtitle?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.placeholder(visible = isLoading)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        val hasUrls = twitterUrl != null || githubUrl != null || linkedinUrl != null || websiteUrl != null
        if (hasUrls) {
            FlowRow(mainAxisSpacing = 16.dp) {
                twitterUrl?.let {
                    Socials.Twitter(
                        text = title,
                        onClick = { twitterUrl.let(onLinkClicked) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
                mastodonUrl?.let {
                    Socials.Mastodon(
                        text = title,
                        onClick = { mastodonUrl.let(onLinkClicked) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
                githubUrl?.let {
                    Socials.GitHub(
                        text = title,
                        onClick = { githubUrl.let(onLinkClicked) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
                linkedinUrl?.let {
                    Socials.LinkedIn(
                        text = title,
                        onClick = { linkedinUrl.let(onLinkClicked) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
                websiteUrl?.let {
                    Socials.Website(
                        text = title,
                        onClick = { websiteUrl.let(onLinkClicked) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
            }
        }
        detailed?.let {
            Spacer(modifier = Modifier.height(12.dp))
            RichTextThemeIntegration(
                textStyle = { MaterialTheme.typography.bodyMedium },
                ProvideTextStyle = null,
                contentColor = { MaterialTheme.colorScheme.onSurface },
                ProvideContentColor = null,
            ) {
                RichText(
                    modifier = Modifier.placeholder(visible = isLoading)
                ) {
                    Markdown(detailed)
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
            title = EventUi.fake.eventInfo.name,
            pronouns = null,
            subtitle = EventUi.fake.eventInfo.date,
            twitterUrl = EventUi.fake.eventInfo.twitterUrl,
            githubUrl = EventUi.fake.eventInfo.twitterUrl,
            linkedinUrl = EventUi.fake.eventInfo.twitterUrl,
            websiteUrl = PartnerItemUi.fake.siteUrl,
            onLinkClicked = {}
        )
    }
}
