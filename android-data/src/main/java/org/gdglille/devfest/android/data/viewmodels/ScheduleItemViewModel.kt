package org.gdglille.devfest.android.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.gdglille.devfest.models.TalkUi
import org.gdglille.devfest.repositories.AgendaRepository

sealed class ScheduleUiState {
    object Loading : ScheduleUiState()
    data class Success(val talk: TalkUi) : ScheduleUiState()
    data class Failure(val throwable: Throwable) : ScheduleUiState()
}

class ScheduleItemViewModel(
    private val scheduleId: String,
    private val repository: AgendaRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ScheduleUiState>(ScheduleUiState.Loading)
    val uiState: StateFlow<ScheduleUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                _uiState.value = ScheduleUiState.Success(repository.scheduleItem(scheduleId))
            } catch (error: Throwable) {
                error.printStackTrace()
                Firebase.crashlytics.recordException(error)
                _uiState.value = ScheduleUiState.Failure(error)
            }
        }
    }

    object Factory {
        fun create(scheduleId: String, repository: AgendaRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST") return ScheduleItemViewModel(
                    scheduleId = scheduleId,
                    repository = repository
                ) as T
            }
        }
    }
}
