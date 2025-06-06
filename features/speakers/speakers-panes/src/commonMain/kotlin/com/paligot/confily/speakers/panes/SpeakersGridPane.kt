package com.paligot.confily.speakers.panes

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
import androidx.compose.ui.unit.dp
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_speakers
import com.paligot.confily.speakers.semantics.SpeakersSemantics
import com.paligot.confily.speakers.ui.models.SpeakerItemUi
import com.paligot.confily.style.components.placeholder.placeholder
import com.paligot.confily.style.speakers.items.LargeSpeakerItem
import com.paligot.confily.style.theme.ConfilyTheme
import com.paligot.confily.style.theme.Scaffold
import com.paligot.confily.style.theme.SpacingTokens
import com.paligot.confily.style.theme.toDp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpeakersGridPane(
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
                        description = it.activity,
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
    ConfilyTheme {
        SpeakersGridPane(
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
