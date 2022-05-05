package org.gdglille.devfest.android.screens.speakers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.gdglille.devfest.models.SpeakerUi
import org.gdglille.devfest.repositories.AgendaRepository

sealed class SpeakerUiState {
    object Loading : SpeakerUiState()
    data class Success(val speaker: SpeakerUi) : SpeakerUiState()
    data class Failure(val throwable: Throwable) : SpeakerUiState()
}

class SpeakerViewModel(
    private val speakerId: String,
    private val repository: AgendaRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<SpeakerUiState>(SpeakerUiState.Loading)
    val uiState: StateFlow<SpeakerUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                _uiState.value = SpeakerUiState.Success(repository.speaker(speakerId))
            } catch (error: Throwable) {
                Firebase.crashlytics.recordException(error)
                _uiState.value = SpeakerUiState.Failure(error)
            }
        }
    }

    object Factory {
        fun create(speakerId: String, repository: AgendaRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SpeakerViewModel(speakerId = speakerId, repository = repository) as T
            }
        }
    }
}
