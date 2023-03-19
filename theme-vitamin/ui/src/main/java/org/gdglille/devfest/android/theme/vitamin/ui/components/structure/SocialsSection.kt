package org.gdglille.devfest.android.theme.vitamin.ui.components.structure

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextThemeIntegration
import org.gdglille.devfest.android.theme.vitamin.ui.components.speakers.Socials
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.placeholder
import org.gdglille.devfest.models.EventUi

@Composable
fun SocialsSection(
    title: String,
    subtitle: String?,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    detailed: String? = null,
    isLoading: Boolean = false,
    twitterUrl: String? = null,
    githubUrl: String? = null,
    linkedinUrl: String? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = VitaminTheme.typography.h4,
            color = VitaminTheme.colors.vtmnContentPrimary,
            modifier = Modifier.placeholder(visible = isLoading)
        )
        subtitle?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = VitaminTheme.typography.body3,
                color = VitaminTheme.colors.vtmnContentSecondary,
                modifier = Modifier.placeholder(visible = isLoading)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (twitterUrl != null || githubUrl != null || linkedinUrl != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                twitterUrl?.let {
                    Socials.Twitter(
                        text = title,
                        onClick = { twitterUrl.let(onLinkClicked) },
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
            }
        }
        detailed?.let {
            Spacer(modifier = Modifier.height(12.dp))
            RichTextThemeIntegration(
                textStyle = { VitaminTheme.typography.body2 },
                ProvideTextStyle = null,
                contentColor = { VitaminTheme.colors.vtmnContentPrimary },
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
            subtitle = EventUi.fake.eventInfo.date,
            twitterUrl = EventUi.fake.eventInfo.twitterUrl,
            githubUrl = EventUi.fake.eventInfo.twitterUrl,
            linkedinUrl = EventUi.fake.eventInfo.twitterUrl,
            onLinkClicked = {}
        )
    }
}
