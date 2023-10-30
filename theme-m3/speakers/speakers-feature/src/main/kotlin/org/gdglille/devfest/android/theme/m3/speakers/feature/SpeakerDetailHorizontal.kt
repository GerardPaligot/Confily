package org.gdglille.devfest.android.theme.m3.speakers.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.schedules.ui.talks.SmallScheduleItem
import org.gdglille.devfest.android.theme.m3.speakers.ui.SpeakerDetailSectionHorizontal
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.appbars.TopAppBar
import org.gdglille.devfest.android.theme.m3.style.placeholder
import org.gdglille.devfest.android.theme.m3.style.previews.PHONE_LANDSCAPE
import org.gdglille.devfest.models.ui.SpeakerUi
import org.gdglille.devfest.models.ui.TalkItemUi

@ExperimentalMaterial3Api
@Composable
fun SpeakerDetailHorizontal(
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(count = 2),
                modifier = Modifier.padding(it),
                contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    SpeakerDetailSectionHorizontal(
                        speaker = speaker,
                        isLoading = isLoading,
                        onLinkClicked = onLinkClicked
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                items(speaker.talks) {
                    SmallScheduleItem(
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

@ExperimentalMaterial3Api
@Preview(device = PHONE_LANDSCAPE)
@Composable
private fun SpeakerDetailPreview() {
    Conferences4HallTheme {
        SpeakerDetailHorizontal(
            speaker = SpeakerUi.fake,
            onTalkClicked = {},
            onFavoriteClicked = {},
            onLinkClicked = {},
            onBackClicked = {}
        )
    }
}
