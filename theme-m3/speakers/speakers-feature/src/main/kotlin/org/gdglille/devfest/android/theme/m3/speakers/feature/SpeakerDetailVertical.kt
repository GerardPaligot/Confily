package org.gdglille.devfest.android.theme.m3.speakers.feature

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.schedules.ui.talks.MediumScheduleItem
import org.gdglille.devfest.android.theme.m3.speakers.ui.SpeakerDetailSectionVertical
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.appbars.TopAppBar
import org.gdglille.devfest.android.theme.m3.style.placeholder
import org.gdglille.devfest.android.theme.m3.style.previews.ThemedPreviews
import org.gdglille.devfest.models.ui.SpeakerUi
import org.gdglille.devfest.models.ui.TalkItemUi

@ExperimentalMaterial3Api
@Composable
fun SpeakerDetailVertical(
    speaker: SpeakerUi,
    onTalkClicked: (id: String) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.screen_speaker_detail),
                navigationIcon = { Back(onClick = onBackClicked) }
            )
        },
        content = {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
                modifier = Modifier.padding(it)
            ) {
                item {
                    SpeakerDetailSectionVertical(
                        speaker = speaker,
                        isLoading = isLoading,
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
    )
}

@Suppress("UnusedPrivateMember")
@ExperimentalMaterial3Api
@ThemedPreviews
@Composable
private fun SpeakerDetailPreview() {
    Conferences4HallTheme {
        SpeakerDetailVertical(
            speaker = SpeakerUi.fake,
            onTalkClicked = {},
            onFavoriteClicked = {},
            onLinkClicked = {},
            onBackClicked = {}
        )
    }
}
