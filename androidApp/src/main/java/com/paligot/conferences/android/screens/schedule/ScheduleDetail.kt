package com.paligot.conferences.android.screens.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paligot.conferences.android.components.appbars.TopAppBar
import com.paligot.conferences.android.components.speakers.SpeakerBox
import com.paligot.conferences.android.components.speakers.SpeakerItem
import com.paligot.conferences.android.components.talks.TalkSection
import com.paligot.conferences.android.components.talks.TalkUi
import com.paligot.conferences.android.components.talks.talk
import com.paligot.conferences.android.theme.ConferenceTheme
import com.paligot.conferences.repositories.AgendaRepository

@Composable
fun ScheduleDetailVM(
    scheduleId: String,
    agendaRepository: AgendaRepository,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onSpeakerClicked: (id: String) -> Unit
) {
    val viewModel: ScheduleItemViewModel = viewModel(
        factory = ScheduleItemViewModel.Factory.create(scheduleId, agendaRepository)
    )
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is ScheduleUiState.Loading -> Text("Loading...")
        is ScheduleUiState.Failure -> Text("Something wrong happened")
        is ScheduleUiState.Success -> ScheduleDetail(
            talk = (uiState.value as ScheduleUiState.Success).talk,
            modifier = modifier,
            onBackClicked = onBackClicked,
            onSpeakerClicked = onSpeakerClicked
        )
    }
}

@Composable
fun ScheduleDetail(
    talk: TalkUi,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onSpeakerClicked: (id: String) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = "Talk Details",
                navigationIcon = { Back(onClick = onBackClicked) }
            )
        },
        content = {
            val contentPadding = 8.dp
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    TalkSection(
                        talk = talk,
                        modifier = Modifier.padding(horizontal = contentPadding)
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        talk.speakers.forEach {
                            SpeakerBox(onClick = { onSpeakerClicked(it.id) }) {
                                SpeakerItem(
                                    speakerUi = it,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = contentPadding, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun TalkDetailPreview() {
    ConferenceTheme {
        ScheduleDetail(
            talk = talk,
            onBackClicked = {},
            onSpeakerClicked = {}
        )
    }
}
