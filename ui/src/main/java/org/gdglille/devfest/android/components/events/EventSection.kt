package org.gdglille.devfest.android.components.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.speakers.Socials
import org.gdglille.devfest.android.theme.placeholder
import org.gdglille.devfest.android.ui.R
import org.gdglille.devfest.models.EventInfoUi

@Composable
fun EventSection(
    logo: Int,
    eventInfo: EventInfoUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    titleTextStyle: TextStyle = MaterialTheme.typography.h6,
    subtitleTextStyle: TextStyle = MaterialTheme.typography.subtitle2,
    color: Color = MaterialTheme.colors.onBackground,
    onFaqClick: (url: String) -> Unit,
    onCoCClick: (url: String) -> Unit,
    onTwitterClick: (url: String?) -> Unit,
    onLinkedInClick: (url: String?) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            Image(
                painter = painterResource(logo),
                contentDescription = null,
                modifier = Modifier.size(96.dp)
            )
            Text(
                text = eventInfo.name,
                style = titleTextStyle,
                color = color,
                modifier = Modifier.placeholder(visible = isLoading)
            )
            Text(
                text = eventInfo.date,
                style = subtitleTextStyle,
                color = color,
                modifier = Modifier.placeholder(visible = isLoading)
            )
        }
        if (eventInfo.twitter != null || eventInfo.linkedin != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                eventInfo.twitter?.let {
                    Socials.Twitter(
                        text = it,
                        onClick = { eventInfo.twitterUrl.let(onTwitterClick) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
                eventInfo.linkedin?.let {
                    Socials.LinkedIn(
                        text = it,
                        onClick = { eventInfo.linkedinUrl.let(onLinkedInClick) },
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
            }
        }
        Text(
            text = eventInfo.address,
            style = subtitleTextStyle,
            color = color,
            modifier = Modifier.placeholder(visible = isLoading)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { onFaqClick(eventInfo.faqLink) },
                modifier = Modifier.fillMaxWidth().weight(1f).placeholder(visible = isLoading)
            ) {
                Text(text = stringResource(R.string.action_faq))
            }
            Button(
                onClick = { onCoCClick(eventInfo.codeOfConductLink) },
                modifier = Modifier.fillMaxWidth().weight(1f).placeholder(visible = isLoading)
            ) {
                Text(text = stringResource(R.string.action_coc))
            }
        }
    }
}
