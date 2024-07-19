package org.gdglille.devfest.android.theme.m3.speakers.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.screen_speakers
import org.gdglille.devfest.android.theme.m3.speakers.semantics.SpeakersSemantics
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.Scaffold
import org.gdglille.devfest.android.theme.m3.style.SpacingTokens
import org.gdglille.devfest.android.theme.m3.style.placeholder.placeholder
import org.gdglille.devfest.theme.m3.style.speakers.items.LargeSpeakerItem
import org.gdglille.devfest.android.theme.m3.style.toDp
import org.gdglille.devfest.models.ui.SpeakerItemUi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
fun SpeakersGridScreen(
    speakers: ImmutableList<SpeakerItemUi>,
    onSpeakerClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    isLoading: Boolean = false
) {
    Scaffold(
        title = stringResource(Resource.string.screen_speakers),
        modifier = modifier,
        hasScrollBehavior = false
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 130.dp),
            modifier = Modifier
                .testTag(SpeakersSemantics.list)
                .padding(top = it.calculateTopPadding())
                .fillMaxWidth(),
            state = state,
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.MediumSpacing.toDp()),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.MediumSpacing.toDp()),
            contentPadding = PaddingValues(
                vertical = SpacingTokens.ExtraLargeSpacing.toDp(),
                horizontal = SpacingTokens.MediumSpacing.toDp()
            ),
            content = {
                items(speakers.toList(), key = { it.id }) {
                    LargeSpeakerItem(
                        name = it.name,
                        description = it.company,
                        url = it.url,
                        onClick = { onSpeakerClicked(it.id) },
                        modifier = Modifier.placeholder(isLoading)
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun SpeakersGridPreview() {
    Conferences4HallTheme {
        SpeakersGridScreen(
            speakers = persistentListOf(
                SpeakerItemUi.fake.copy(id = "1"),
                SpeakerItemUi.fake.copy(id = "2"),
                SpeakerItemUi.fake.copy(id = "3"),
                SpeakerItemUi.fake.copy(id = "4")
            ),
            onSpeakerClicked = {}
        )
    }
}
