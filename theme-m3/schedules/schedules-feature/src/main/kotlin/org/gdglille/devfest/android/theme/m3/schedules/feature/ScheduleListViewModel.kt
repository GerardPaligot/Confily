package org.gdglille.devfest.android.theme.m3.schedules.feature

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gdglille.devfest.AlarmScheduler
import org.gdglille.devfest.models.ui.AgendaUi
import org.gdglille.devfest.models.ui.TalkItemUi
import org.gdglille.devfest.repositories.AgendaRepository

sealed class ScheduleListUiState {
    data class Loading(val agenda: ImmutableList<AgendaUi>) : ScheduleListUiState()
    data class Success(val agenda: ImmutableList<AgendaUi>) : ScheduleListUiState()
    data class Failure(val throwable: Throwable) : ScheduleListUiState()
}

@FlowPreview
@ExperimentalCoroutinesApi
class ScheduleListViewModel(
    private val repository: AgendaRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    val uiState: StateFlow<ScheduleListUiState> = repository.agenda()
        .map {
            if (it.isNotEmpty()) {
                ScheduleListUiState.Success(it.values.toImmutableList())
            } else {
                ScheduleListUiState.Success(persistentListOf())
            }
        }
        .catch {
            Firebase.crashlytics.recordException(it)
            ScheduleListUiState.Failure(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = ScheduleListUiState.Loading(persistentListOf(AgendaUi.fake)),
            started = SharingStarted.WhileSubscribed()
        )

    @SuppressLint("UnspecifiedImmutableFlag")
    fun markAsFavorite(context: Context, talkItem: TalkItemUi) = viewModelScope.launch {
        alarmScheduler.schedule(context, talkItem)
    }

    object Factory {
        fun create(
            repository: AgendaRepository,
            alarmScheduler: AlarmScheduler
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = ScheduleListViewModel(
                repository = repository,
                alarmScheduler = alarmScheduler
            ) as T
        }
    }
}
