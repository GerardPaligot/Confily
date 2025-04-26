package com.paligot.confily.speakers.panes

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
import com.paligot.confily.schedules.ui.models.TalkItemUi
import com.paligot.confily.schedules.ui.talks.MediumScheduleItem
import com.paligot.confily.speakers.panes.models.SpeakerUi
import com.paligot.confily.speakers.ui.SpeakerDetailSectionVertical
import com.paligot.confily.style.components.placeholder.placeholder
import com.paligot.confily.style.theme.ConfilyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SpeakerDetailContent(
    speaker: SpeakerUi,
    onTalkClicked: (id: String) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isLoading: Boolean = false,
    displayAvatar: Boolean = true
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
        state = state,
        modifier = modifier.padding(contentPadding)
    ) {
        item {
            SpeakerDetailSectionVertical(
                info = speaker.info,
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

@Preview
@Composable
private fun SpeakerDetailScreenPreview() {
    ConfilyTheme {
        Scaffold {
            SpeakerDetailContent(
                speaker = SpeakerUi.fake,
                onTalkClicked = {},
                onFavoriteClicked = {},
                onLinkClicked = {},
                contentPadding = it
            )
        }
    }
}
