package org.gdglille.devfest.android.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.speakers.SpeakerDescriptionSection
import org.gdglille.devfest.android.components.speakers.SpeakerHeader
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.models.SpeakerUi

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SpeakerDetail(
    speaker: SpeakerUi,
    modifier: Modifier = Modifier,
    onLinkClicked: (url: String) -> Unit,
    onBackClicked: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        content = {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    SpeakerHeader(
                        url = speaker.url,
                        name = speaker.name,
                        company = speaker.company,
                        onBackClicked = onBackClicked
                    )
                }
                item {
                    SpeakerDescriptionSection(
                        speaker = speaker,
                        onTwitterClick = onLinkClicked,
                        onGitHubClick = onLinkClicked
                    )
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
            onLinkClicked = {},
            onBackClicked = {}
        )
    }
}
