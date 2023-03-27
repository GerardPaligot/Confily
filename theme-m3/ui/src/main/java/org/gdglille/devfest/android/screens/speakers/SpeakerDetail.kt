package org.gdglille.devfest.android.screens.speakers

import android.annotation.SuppressLint
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.appbars.TopAppBar
import org.gdglille.devfest.android.components.speakers.SpeakerDetailSection
import org.gdglille.devfest.android.components.talks.TalkItem
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.placeholder
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.SpeakerUi
import org.gdglille.devfest.models.TalkItemUi

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SpeakerDetail(
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
            LazyColumn(contentPadding = it) {
                item {
                    SpeakerDetailSection(
                        speaker = speaker,
                        isLoading = isLoading,
                        onLinkClicked = onLinkClicked
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                items(speaker.talks) {
                    TalkItem(
                        talk = it,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .placeholder(visible = isLoading),
                        onTalkClicked = onTalkClicked,
                        onFavoriteClicked = onFavoriteClicked
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    )
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun SpeakerListPreview() {
    Conferences4HallTheme {
        SpeakerDetail(
            speaker = SpeakerUi.fake,
            onTalkClicked = {},
            onFavoriteClicked = {},
            onLinkClicked = {},
            onBackClicked = {}
        )
    }
}
