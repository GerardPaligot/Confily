package org.gdglille.devfest.android.theme.m3.speakers.feature

import android.content.res.Configuration
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import kotlinx.collections.immutable.ImmutableList
import org.gdglille.devfest.models.ui.SpeakerItemUi

@Composable
fun SpeakersListOrientable(
    speakers: ImmutableList<SpeakerItemUi>,
    onSpeakerClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    val orientation = LocalConfiguration.current
    val state = rememberLazyGridState()
    if (orientation.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        SpeakersList(
            speakers = speakers,
            columnCount = 4,
            modifier = modifier,
            state = state,
            onSpeakerClicked = onSpeakerClicked,
            isLoading = isLoading
        )
    } else {
        SpeakersList(
            speakers = speakers,
            columnCount = 2,
            modifier = modifier,
            state = state,
            onSpeakerClicked = onSpeakerClicked,
            isLoading = isLoading
        )
    }
}
