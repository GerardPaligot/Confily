package org.gdglille.devfest.android.data.viewmodels

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.gdglille.devfest.android.data.AlarmScheduler
import org.gdglille.devfest.models.AgendaUi
import org.gdglille.devfest.models.TalkItemUi
import org.gdglille.devfest.repositories.AgendaRepository

sealed class AgendaUiState {
    data class Loading(val agenda: ImmutableList<AgendaUi>) : AgendaUiState()
    data class Success(val agenda: ImmutableList<AgendaUi>) : AgendaUiState()
    data class Failure(val throwable: Throwable) : AgendaUiState()
}

@FlowPreview
@ExperimentalCoroutinesApi
class AgendaViewModel(
    private val dates: List<String>,
    private val repository: AgendaRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    private val _uiState = MutableStateFlow<AgendaUiState>(
        AgendaUiState.Loading(persistentListOf(AgendaUi.fake))
    )
    val uiState: StateFlow<AgendaUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                combine(dates.map { repository.agenda(date = it) }) {
                    it.toList().toImmutableList()
                }.collect {
                    _uiState.value = AgendaUiState.Success(it)
                }
            } catch (error: Throwable) {
                Firebase.crashlytics.recordException(error)
                _uiState.value = AgendaUiState.Failure(error)
            }
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun markAsFavorite(context: Context, talkItem: TalkItemUi) = viewModelScope.launch {
        alarmScheduler.schedule(context, talkItem)
    }

    object Factory {
        fun create(
            dates: List<String>,
            repository: AgendaRepository,
            alarmScheduler: AlarmScheduler
        ) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T = AgendaViewModel(
                    dates = dates,
                    repository = repository,
                    alarmScheduler = alarmScheduler
                ) as T
            }
    }
}
