package org.gdglille.devfest.android.theme.m3.speakers.screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.screen_speaker_detail
import org.gdglille.devfest.android.theme.m3.style.Scaffold
import org.gdglille.devfest.android.theme.m3.style.appbars.AppBarIcons
import org.gdglille.devfest.models.ui.SpeakerUi
import org.gdglille.devfest.models.ui.TalkItemUi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalResourceApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun SpeakerDetailOrientable(
    speaker: SpeakerUi,
    onTalkClicked: (id: String) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
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
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                    isLoading = isLoading,
                    modifier = Modifier.weight(1f)
                )
                SpeakerDetailScreen(
                    speaker = speaker,
                    onTalkClicked = onTalkClicked,
                    onFavoriteClicked = onFavoriteClicked,
                    onLinkClicked = onLinkClicked,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
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
                isLoading = isLoading,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
            )
        }
    }
}
