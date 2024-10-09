package com.paligot.confily.wear.schedules.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.schedules.SchedulesRepository
import com.paligot.confily.models.ui.AgendaUi
import com.paligot.confily.models.ui.EventSessionItemUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.wear.schedules.panes.EventSessionModelUi
import com.paligot.confily.wear.schedules.panes.ScheduleModelUi
import com.paligot.confily.wear.schedules.panes.ScheduleSessionModelUi
import com.paligot.confily.wear.schedules.panes.SessionModelUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class SchedulesUiState {
    data object Loading : SchedulesUiState()
    data class Success(val modelUi: ScheduleModelUi) : SchedulesUiState()
}

class SchedulesViewModel(repository: SchedulesRepository) : ViewModel() {
    val uiState = repository.agenda()
        .map { agenda ->
            ScheduleModelUi(
                sessions = agenda.entries.associate { it.key to it.value.toModelUi() }
                    .toImmutableMap()
            )
        }
        .map { SchedulesUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = SchedulesUiState.Loading
        )
}

private fun AgendaUi.toModelUi(): ImmutableList<SessionModelUi> {
    return this.sessions.values.flatten()
        .map {
            when (it) {
                is TalkItemUi -> it.toModelUi()
                is EventSessionItemUi -> it.toModelUi()
            }
        }
        .toImmutableList()
}

private fun TalkItemUi.toModelUi() = ScheduleSessionModelUi(
    id = this.id,
    title = this.title,
    timeSlot = this.slotTime,
    timeDuration = this.time,
    categoryUi = this.category
)

private fun EventSessionItemUi.toModelUi() = EventSessionModelUi(
    title = this.title,
    timeSlot = this.slotTime,
    timeDuration = this.time
)
