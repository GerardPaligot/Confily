package org.gdglille.devfest.android.theme.m3.speakers.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.models.ui.SpeakerUi
import org.gdglille.devfest.models.ui.TalkItemUi

@Composable
fun SpeakerDetailOrientable(
    speaker: SpeakerUi,
    onTalkClicked: (id: String) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isLoading: Boolean = false
) {
    val state = rememberLazyListState()
    val orientation = LocalConfiguration.current
    if (orientation.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = modifier.padding(contentPadding)
        ) {
            SpeakerAvatarScreen(
                url = speaker.url,
                isLoading = isLoading,
                modifier = Modifier.weight(1f)
            )
            SpeakerDetailVerticalScreen(
                speaker = speaker,
                onTalkClicked = onTalkClicked,
                onFavoriteClicked = onFavoriteClicked,
                onLinkClicked = onLinkClicked,
                modifier = Modifier.weight(1f),
                state = state,
                isLoading = isLoading,
                displayAvatar = false
            )
        }
    } else {
        SpeakerDetailVerticalScreen(
            speaker = speaker,
            onTalkClicked = onTalkClicked,
            onFavoriteClicked = onFavoriteClicked,
            onLinkClicked = onLinkClicked,
            modifier = modifier,
            state = state,
            contentPadding = contentPadding,
            isLoading = isLoading
        )
    }
}
