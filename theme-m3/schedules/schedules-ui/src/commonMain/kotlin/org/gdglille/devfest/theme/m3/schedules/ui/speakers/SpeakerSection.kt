package org.gdglille.devfest.theme.m3.schedules.ui.speakers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_speaker_title
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.gdglille.devfest.models.ui.SpeakerItemUi
import org.jetbrains.compose.resources.stringResource

@Composable
fun SpeakerSection(
    speakers: ImmutableList<SpeakerItemUi>,
    onSpeakerItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    subtitleTextStyle: TextStyle = MaterialTheme.typography.titleLarge
) {
    val speakersChunked = remember(speakers) {
        speakers.chunked(size = 2).map { it.toImmutableList() }
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(Resource.string.text_speaker_title),
            style = subtitleTextStyle
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
