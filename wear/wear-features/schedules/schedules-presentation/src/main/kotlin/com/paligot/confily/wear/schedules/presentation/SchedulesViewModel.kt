package com.paligot.confily.wear.schedules.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.schedules.SessionRepository
import com.paligot.confily.core.schedules.entities.EventSessionItem
import com.paligot.confily.core.schedules.entities.SessionItem
import com.paligot.confily.core.schedules.entities.Sessions
import com.paligot.confily.core.schedules.entities.mapToUi
import com.paligot.confily.wear.schedules.panes.EventSessionModelUi
import com.paligot.confily.wear.schedules.panes.ScheduleModelUi
import com.paligot.confily.wear.schedules.panes.ScheduleSessionModelUi
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant

sealed class SchedulesUiState {
    data object Loading : SchedulesUiState()
    data class Success(val modelUi: ScheduleModelUi) : SchedulesUiState()
}

class SchedulesViewModel(repository: SessionRepository) : ViewModel() {
    val uiState = repository.sessions()
        .map { SchedulesUiState.Success(it.mapToUi()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = SchedulesUiState.Loading
        )
}

private fun Sessions.mapToUi(): ScheduleModelUi = ScheduleModelUi(
    sessions = this.sessions
        .map {
            it.key.format(
                LocalDate.Format {
                    byUnicodePattern("dd/MM/yyyy")
                }
            ) to it.value.map { session ->
                when (session) {
                    is SessionItem -> session.mapToUi()
                    is EventSessionItem -> session.mapToUi()
                }
            }.toImmutableList()
        }
        .associate { it }
        .toImmutableMap()
)

private fun SessionItem.mapToUi(): ScheduleSessionModelUi {
    val diff = endTime.toInstant(TimeZone.currentSystemDefault())
        .minus(startTime.toInstant(TimeZone.currentSystemDefault()))
    val timeInMinutes = diff.inWholeMinutes.toInt()
    return ScheduleSessionModelUi(
        id = this.id,
        title = this.title,
        timeSlot = startTime.format(
            LocalDateTime.Format {
                hour()
                char(':')
                minute()
            }
        ),
        timeDuration = timeInMinutes.toString(),
        categoryUi = this.category.mapToUi()
    )
}

private fun EventSessionItem.mapToUi(): EventSessionModelUi {
    val diff = endTime.toInstant(TimeZone.currentSystemDefault())
        .minus(startTime.toInstant(TimeZone.currentSystemDefault()))
    val timeInMinutes = diff.inWholeMinutes.toInt()
    return EventSessionModelUi(
        title = this.title,
        timeSlot = startTime.format(
            LocalDateTime.Format {
                hour()
                char(':')
                minute()
            }
        ),
        timeDuration = timeInMinutes.toString()
    )
}
