package com.paligot.confily.speakers.panes

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_speaker_detail
import com.paligot.confily.schedules.ui.models.TalkItemUi
import com.paligot.confily.speakers.panes.models.SpeakerUi
import com.paligot.confily.style.theme.Scaffold
import com.paligot.confily.style.theme.appbars.AppBarIcons
import org.jetbrains.compose.resources.stringResource

@Composable
fun SpeakerDetailPane(
    speaker: SpeakerUi,
    onTalkClicked: (id: String) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (AppBarIcons.() -> Unit)? = null,
    isLandscape: Boolean = false,
    isLoading: Boolean = false
) {
    val state = rememberLazyListState()
    Scaffold(
        title = stringResource(Resource.string.screen_speaker_detail),
        navigationIcon = navigationIcon,
        modifier = modifier
    ) {
        if (isLandscape) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(it)
            ) {
                SpeakerAvatarContent(
                    url = speaker.info.url,
                    isLoading = isLoading,
                    modifier = Modifier.weight(1f)
                )
                SpeakerDetailContent(
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
            SpeakerDetailContent(
                speaker = speaker,
                onTalkClicked = onTalkClicked,
                onFavoriteClicked = onFavoriteClicked,
                onLinkClicked = onLinkClicked,
                state = state,
                contentPadding = it,
                isLoading = isLoading
            )
        }
    }
}
