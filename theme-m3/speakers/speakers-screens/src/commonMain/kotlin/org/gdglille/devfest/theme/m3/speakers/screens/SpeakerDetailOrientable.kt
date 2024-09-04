package org.gdglille.devfest.theme.m3.speakers.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paligot.confily.models.ui.SpeakerUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_speaker_detail
import com.paligot.confily.style.theme.Scaffold
import com.paligot.confily.style.theme.appbars.AppBarIcons
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpeakerDetailOrientable(
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
                SpeakerAvatarScreen(
                    url = speaker.url,
                    isLoading = isLoading,
                    modifier = Modifier.weight(1f)
                )
                SpeakerDetailScreen(
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
            SpeakerDetailScreen(
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
