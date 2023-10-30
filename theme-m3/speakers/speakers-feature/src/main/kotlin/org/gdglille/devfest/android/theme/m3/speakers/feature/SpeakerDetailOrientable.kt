package org.gdglille.devfest.android.theme.m3.speakers.feature

import android.content.res.Configuration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import org.gdglille.devfest.models.ui.SpeakerUi
import org.gdglille.devfest.models.ui.TalkItemUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakerDetailOrientable(
    speaker: SpeakerUi,
    onTalkClicked: (id: String) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    val orientation = LocalConfiguration.current
    if (orientation.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        SpeakerDetailHorizontal(
            speaker = speaker,
            modifier = modifier,
            onTalkClicked = onTalkClicked,
            onFavoriteClicked = onFavoriteClicked,
            onLinkClicked = onLinkClicked,
            onBackClicked = onBackClicked,
            isLoading = isLoading
        )
    } else {
        SpeakerDetailVertical(
            speaker = speaker,
            modifier = modifier,
            onTalkClicked = onTalkClicked,
            onFavoriteClicked = onFavoriteClicked,
            onLinkClicked = onLinkClicked,
            onBackClicked = onBackClicked,
            isLoading = isLoading
        )
    }
}
