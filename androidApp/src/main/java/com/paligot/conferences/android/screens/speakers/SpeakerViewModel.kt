package com.paligot.conferences.android.screens.speakers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.paligot.conferences.repositories.AgendaRepository
import com.paligot.conferences.repositories.SpeakerUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
            } catch (ignore: Throwable) {
                _uiState.value = SpeakerUiState.Failure(ignore)
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
