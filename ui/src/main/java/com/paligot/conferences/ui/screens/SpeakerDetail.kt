package com.paligot.conferences.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.conferences.repositories.SpeakerUi
import com.paligot.conferences.ui.components.speakers.SpeakerHeader
import com.paligot.conferences.ui.components.speakers.SpeakerSection
import com.paligot.conferences.ui.components.speakers.speaker
import com.paligot.conferences.ui.theme.Conferences4HallTheme

@Composable
fun SpeakerDetail(
    speaker: SpeakerUi,
    modifier: Modifier = Modifier,
    onTwitterClick: (url: String) -> Unit,
    onGitHubClick: (url: String) -> Unit,
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
                    SpeakerSection(
                        speaker = speaker,
                        onTwitterClick = onTwitterClick,
                        onGitHubClick = onGitHubClick
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun SpeakerListPreview() {
    Conferences4HallTheme {
        SpeakerDetail(
            speaker = speaker,
            onTwitterClick = {},
            onGitHubClick = {},
            onBackClicked = {}
        )
    }
}
