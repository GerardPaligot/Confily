package org.gdglille.devfest.android.components.speakers

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.models.SpeakerItemUi

@Composable
fun SpeakerBox(
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication = LocalIndication.current,
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
    color: Color = MaterialTheme.colorScheme.onBackground,
    nameTextStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
    companyTextStyle: TextStyle = MaterialTheme.typography.bodySmall
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
            CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(alpha = .73f)) {
                Text(
                    text = speakerUi.company,
                    color = color,
                    style = companyTextStyle
                )
            }
        }
    }
}

@Preview
@Composable
fun SpeakerItemPreview() {
    Conferences4HallTheme {
        SpeakerBox(onClick = {}) {
            SpeakerItem(speakerUi = SpeakerItemUi.fake)
        }
    }
}
