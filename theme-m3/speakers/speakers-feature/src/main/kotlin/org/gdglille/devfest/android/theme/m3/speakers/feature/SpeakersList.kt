package org.gdglille.devfest.android.theme.m3.speakers.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.SpacingTokens
import org.gdglille.devfest.android.theme.m3.style.placeholder
import org.gdglille.devfest.android.theme.m3.style.speakers.items.LargeSpeakerItem
import org.gdglille.devfest.android.theme.m3.style.toDp
import org.gdglille.devfest.models.ui.SpeakerItemUi

@Composable
fun SpeakersList(
    speakers: ImmutableList<SpeakerItemUi>,
    onSpeakerClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.MediumSpacing.toDp()),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.MediumSpacing.toDp()),
        contentPadding = PaddingValues(SpacingTokens.LargeSpacing.toDp()),
        content = {
            items(speakers.toList()) {
                LargeSpeakerItem(
                    name = it.name,
                    description = it.company,
                    url = it.url,
                    onClick = { onSpeakerClicked(it.id) },
                    modifier =  Modifier.placeholder(isLoading)
                )
            }
        }
    )
}

@Preview
@Composable
private fun SpeakersListPreview() {
    Conferences4HallTheme {
        SpeakersList(
            speakers = persistentListOf(
                SpeakerItemUi.fake,
                SpeakerItemUi.fake,
                SpeakerItemUi.fake,
                SpeakerItemUi.fake
            ),
            onSpeakerClicked = {}
        )
    }
}
