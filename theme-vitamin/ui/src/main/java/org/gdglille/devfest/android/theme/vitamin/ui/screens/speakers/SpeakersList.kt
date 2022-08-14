package org.gdglille.devfest.android.theme.vitamin.ui.screens.speakers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.vitamin.ui.components.speakers.SpeakerItemRow
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.models.SpeakerItemUi

@Composable
fun SpeakersList(
    speakers: List<List<SpeakerItemUi>>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onSpeakerClicked: (id: String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        speakers.forEach {
            item {
                SpeakerItemRow(
                    speakers = it,
                    isLoading = isLoading,
                    onSpeakerItemClick = { onSpeakerClicked(it.id) }
                )
            }
        }
    }
}

@Preview
@Composable
fun SpeakersListPreview() {
    Conferences4HallTheme {
        SpeakersList(
            speakers = arrayListOf(
                arrayListOf(SpeakerItemUi.fake, SpeakerItemUi.fake),
                arrayListOf(SpeakerItemUi.fake, SpeakerItemUi.fake)
            ),
            onSpeakerClicked = {}
        )
    }
}
