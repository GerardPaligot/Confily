package org.gdglille.devfest.android.theme.vitamin.ui.components.speakers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.placeholder
import org.gdglille.devfest.models.SpeakerItemUi

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpeakerItem(
    speakerUi: SpeakerItemUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onClick: (SpeakerItemUi) -> Unit
) {
    Card(
        modifier = modifier.placeholder(visible = isLoading),
        shape = RoundedCornerShape(size = 8.dp),
        onClick = { onClick(speakerUi) },
        elevation = 2.dp
    ) {
        Column {
            SpeakerAvatar(
                url = speakerUi.url,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f)
            )
            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = speakerUi.name,
                        style = VitaminTheme.typography.body2,
                        fontWeight = FontWeight.Bold,
                        color = VitaminTheme.colors.vtmnContentPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    speakerUi.pronouns?.let {
                        Text(
                            text = it,
                            style = VitaminTheme.typography.body3,
                            color = VitaminTheme.colors.vtmnContentPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Text(
                    text = speakerUi.company,
                    style = VitaminTheme.typography.body3,
                    color = VitaminTheme.colors.vtmnContentSecondary,
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
        SpeakerItem(
            speakerUi = SpeakerItemUi.fake,
            onClick = {}
        )
    }
}
