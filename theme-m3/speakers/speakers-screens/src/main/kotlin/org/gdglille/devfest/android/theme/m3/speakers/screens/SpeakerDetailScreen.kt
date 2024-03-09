package org.gdglille.devfest.android.theme.m3.speakers.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.schedules.ui.talks.MediumScheduleItem
import org.gdglille.devfest.android.theme.m3.speakers.ui.SpeakerDetailSectionVertical
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.placeholder.placeholder
import org.gdglille.devfest.android.theme.m3.style.previews.ThemedPreviews
import org.gdglille.devfest.models.ui.SpeakerUi
import org.gdglille.devfest.models.ui.TalkItemUi

@Composable
fun SpeakerDetailScreen(
    speaker: SpeakerUi,
    onTalkClicked: (id: String) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isLoading: Boolean = false,
    displayAvatar: Boolean = true,
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
        state = state,
        modifier = modifier.padding(contentPadding)
    ) {
        item {
            SpeakerDetailSectionVertical(
                speaker = speaker,
                isLoading = isLoading,
                displayAvatar = displayAvatar,
                onLinkClicked = onLinkClicked
            )
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        items(speaker.talks) {
            MediumScheduleItem(
                talk = it,
                modifier = Modifier
                    .placeholder(visible = isLoading),
                onFavoriteClicked = onFavoriteClicked,
                onTalkClicked = onTalkClicked
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Suppress("UnusedPrivateMember")
@ThemedPreviews
@Composable
private fun SpeakerDetailScreenPreview() {
    Conferences4HallTheme {
        Scaffold {
            SpeakerDetailScreen(
                speaker = SpeakerUi.fake,
                onTalkClicked = {},
                onFavoriteClicked = {},
                onLinkClicked = {},
                contentPadding = it
            )
        }
    }
}
