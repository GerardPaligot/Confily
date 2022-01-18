package com.paligot.conferences.android.components.events

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.conferences.android.components.speakers.Socials
import com.paligot.conferences.android.screens.event.event
import com.paligot.conferences.android.theme.Conferences4HallTheme

data class EventInfoUi(
    @DrawableRes
    val logo: Int,
    val name: String,
    val address: String,
    val date: String,
    val twitter: String?,
    val twitterUrl: String?,
    val linkedin: String?,
    val linkedinUrl: String?,
    val faqLink: String,
    val codeOfConductLink: String,
)

@Composable
fun EventSection(
    eventInfo: EventInfoUi,
    modifier: Modifier = Modifier,
    titleTextStyle: TextStyle = MaterialTheme.typography.h6,
    subtitleTextStyle: TextStyle = MaterialTheme.typography.subtitle2,
    color: Color = MaterialTheme.colors.onBackground,
    onFaqClick: (url: String) -> Unit,
    onCoCClick: (url: String) -> Unit,
    onTwitterClick: (url: String) -> Unit,
    onLinkedInClick: (url: String) -> Unit,
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
                painter = painterResource(eventInfo.logo),
                contentDescription = null,
                modifier = Modifier.size(96.dp)
            )
            Text(text = eventInfo.name, style = titleTextStyle, color = color)
            Text(text = eventInfo.date, style = subtitleTextStyle, color = color)
        }
        if (eventInfo.twitter != null || eventInfo.linkedin != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                eventInfo.twitter?.let {
                    Socials.Twitter(
                        text = it,
                        onClick = { eventInfo.twitter.let(onTwitterClick) }
                    )
                }
                eventInfo.linkedin?.let {
                    Socials.LinkedIn(
                        text = it,
                        onClick = { eventInfo.linkedin.let(onLinkedInClick) }
                    )
                }
            }
        }
        Text(text = eventInfo.address, style = subtitleTextStyle, color = color)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { onFaqClick(eventInfo.faqLink) },
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                Text("FAQ")
            }
            Button(
                onClick = { onCoCClick(eventInfo.codeOfConductLink) },
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                Text("Code Of Conduct")
            }
        }
    }
}


@Preview
@Composable
fun EventSectionPreview() {
    Conferences4HallTheme {
        Scaffold {
            EventSection(
                eventInfo = event.eventInfo,
                onFaqClick = {},
                onCoCClick = {},
                onTwitterClick = {},
                onLinkedInClick = {},
            )
        }
    }
}
