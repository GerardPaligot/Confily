package org.gdglille.devfest.android.theme.vitamin.ui.screens.speakers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.vitamin.ui.components.speakers.SpeakerItemRow
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.models.SpeakerItemUi

@Composable
fun SpeakersList(
    speakers: ImmutableList<ImmutableList<SpeakerItemUi>>,
    onSpeakerClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
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
            speakers = persistentListOf(
                persistentListOf(SpeakerItemUi.fake, SpeakerItemUi.fake),
                persistentListOf(SpeakerItemUi.fake, SpeakerItemUi.fake)
            ),
            onSpeakerClicked = {}
        )
    }
}
