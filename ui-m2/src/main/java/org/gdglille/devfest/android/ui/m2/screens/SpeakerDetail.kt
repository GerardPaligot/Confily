package org.gdglille.devfest.android.ui.m2.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.speakers.m2.SpeakerSection
import org.gdglille.devfest.android.ui.m2.components.speakers.SpeakerHeader
import org.gdglille.devfest.android.ui.m2.theme.Conferences4HallTheme
import org.gdglille.devfest.models.SpeakerUi

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
            speaker = SpeakerUi.fake,
            onTwitterClick = {},
            onGitHubClick = {},
            onBackClicked = {}
        )
    }
}
