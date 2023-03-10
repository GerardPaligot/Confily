package org.gdglille.devfest.android.components.speakers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.placeholder
import org.gdglille.devfest.models.SpeakerItemUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakerItem(
    speakerUi: SpeakerItemUi,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    nameTextStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
    companyTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    isLoading: Boolean = false,
    onClick: (SpeakerItemUi) -> Unit
) {
    Card(
        modifier = modifier.placeholder(visible = isLoading),
        shape = MaterialTheme.shapes.small,
        onClick = { onClick(speakerUi) }
    ) {
        Column {
            SpeakerAvatar(
                url = speakerUi.url,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                shape = MaterialTheme.shapes.extraSmall
                    .copy(bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
            )
            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = speakerUi.name,
                    color = color,
                    style = nameTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = speakerUi.company,
                    color = color,
                    style = companyTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun SpeakerItemPreview() {
    Conferences4HallTheme {
        SpeakerItem(speakerUi = SpeakerItemUi.fake) {}
    }
}
