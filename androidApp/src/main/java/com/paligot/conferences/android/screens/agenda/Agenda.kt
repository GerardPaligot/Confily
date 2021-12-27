package com.paligot.conferences.android.screens.agenda

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paligot.conferences.android.components.appbars.TopAppBar
import com.paligot.conferences.android.components.talks.ScheduleItem
import com.paligot.conferences.android.components.talks.TalkItemUi
import com.paligot.conferences.android.components.talks.talkItem
import com.paligot.conferences.android.theme.ConferenceTheme
import com.paligot.conferences.repositories.AgendaRepository

data class AgendaUi(val talks: Map<String, List<TalkItemUi>>)

@Composable
fun AgendaVM(
  agendaRepository: AgendaRepository,
  modifier: Modifier = Modifier,
  onTalkClicked: (id: String) -> Unit
) {
  val viewModel: AgendaViewModel = viewModel(
    factory = AgendaViewModel.Factory.create(agendaRepository)
  )
  val uiState = viewModel.uiState.collectAsState()
  when (uiState.value) {
    is AgendaUiState.Loading -> Text("Loading...")
    is AgendaUiState.Failure -> Text("Something wrong happened")
    is AgendaUiState.Success -> Agenda(
      agenda = (uiState.value as AgendaUiState.Success).agenda,
      modifier = modifier,
      onTalkClicked = onTalkClicked
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Agenda(
  agenda: AgendaUi,
  modifier: Modifier = Modifier,
  onTalkClicked: (id: String) -> Unit
) {
  Scaffold(
    modifier = modifier,
    topBar = { TopAppBar(title = "Agenda") },
    content = {
      LazyColumn(
        contentPadding = PaddingValues(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        items(agenda.talks.keys.toList()) {
          ScheduleItem(time = it, talks = agenda.talks[it]!!, onTalkClicked = onTalkClicked)
        }
      }
    }
  )
}

@Preview
@Composable
fun AgendaPreview() {
  ConferenceTheme {
    Agenda(
      agenda = AgendaUi(
        talks = mapOf(
          "10:00" to arrayListOf(talkItem, talkItem),
          "11:00" to arrayListOf(talkItem, talkItem),
          "12:00" to arrayListOf(talkItem, talkItem),
          "13:00" to arrayListOf(talkItem, talkItem),
          "14:00" to arrayListOf(talkItem, talkItem),
          "15:00" to arrayListOf(talkItem, talkItem),
        ),
      ),
      onTalkClicked = {}
    )
  }
}
