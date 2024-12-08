package com.paligot.confily.wear.schedules.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.schedules.SessionRepository
import com.paligot.confily.core.schedules.entities.Session
import com.paligot.confily.core.speakers.entities.SpeakerItem
import com.paligot.confily.models.ui.SpeakerItemUi
import com.paligot.confily.resources.Strings
import com.paligot.confily.wear.schedules.panes.SessionDetailModelUi
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

sealed class ScheduleUiState {
    data object Loading : ScheduleUiState()
    data class Success(val modelUi: SessionDetailModelUi) : ScheduleUiState()
}

class ScheduleViewModel(
    sessionId: String,
    repository: SessionRepository,
    strings: Strings
) : ViewModel() {
    val uiState = repository.session(sessionId)
        .map { ScheduleUiState.Success(it.mapToSessionDetailModelUi(strings)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ScheduleUiState.Loading
        )
}

private fun Session.mapToSessionDetailModelUi(strings: Strings) = SessionDetailModelUi(
    title = title,
    slotTime = startTime.format(
        LocalDateTime.Format {
            hour()
            char(':')
            minute()
        }
    ),
    room = room,
    abstract = abstract,
    speakers = speakers.map { it.mapToSpeakerItemUi(strings) }.toImmutableList()
)

private fun SpeakerItem.mapToSpeakerItemUi(strings: Strings) = SpeakerItemUi(
    id = id,
    name = displayName,
    activity = displayActivity(strings) ?: "",
    url = photoUrl
)

private fun SpeakerItem.displayActivity(strings: Strings) = when {
    jobTitle != null && company != null -> strings.texts.speakerActivity(jobTitle!!, company!!)
    jobTitle == null && company != null -> company
    jobTitle != null && company == null -> jobTitle
    else -> null
}
