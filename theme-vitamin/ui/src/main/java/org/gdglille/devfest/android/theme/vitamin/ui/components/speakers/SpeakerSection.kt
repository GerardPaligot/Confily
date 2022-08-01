package org.gdglille.devfest.android.theme.vitamin.ui.components.speakers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.R
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.models.SpeakerItemUi

@Composable
fun SpeakerSection(
    speakers: List<SpeakerItemUi>,
    modifier: Modifier = Modifier,
    onSpeakerItemClick: (String) -> Unit,
) {
    val speakersChunked = remember(speakers) { speakers.chunked(size = 2) }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.text_speaker_title),
            style = VitaminTheme.typography.h6
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            speakersChunked.forEach {
                SpeakerItemRow(
                    speakers = it,
                    onSpeakerItemClick = { onSpeakerItemClick(it.id) }
                )
            }
        }
    }
}

@Preview
@Composable
fun SpeakerSectionPreview() {
    Conferences4HallTheme {
        SpeakerSection(
            speakers = arrayListOf(SpeakerItemUi.fake, SpeakerItemUi.fake),
            onSpeakerItemClick = {}
        )
    }
}
