package com.paligot.conferences.android.components.speakers

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.conferences.android.theme.ConferenceTheme

data class SpeakerItemUi(
    val id: String,
    val name: String,
    val company: String,
    val url: String
)

val speakerItem = SpeakerItemUi(
    id = "1",
    name = "Gérard Paligot",
    company = "Decathlon",
    url = "https://pbs.twimg.com/profile_images/1465658195767136257/zdYQWsTj_400x400.jpg"
)

@Composable
fun SpeakerBox(
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication = rememberRipple(),
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = indication,
            onClick = onClick,
            role = Role.Button
        ),
        content = content
    )
}

@Composable
fun SpeakerItem(
    speakerUi: SpeakerItemUi,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground,
    nameTextStyle: TextStyle = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
    companyTextStyle: TextStyle = MaterialTheme.typography.caption
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SpeakerAvatar(
            url = speakerUi.url,
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Column {
            Text(text = speakerUi.name, color = color, style = nameTextStyle)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = speakerUi.company,
                    color = color.copy(LocalContentAlpha.current),
                    style = companyTextStyle
                )
            }
        }
    }
}

@Preview
@Composable
fun SpeakerItemPreview() {
    ConferenceTheme {
        SpeakerBox(onClick = {}) {
            SpeakerItem(speakerUi = speakerItem)
        }
    }
}
