package com.paligot.confily.wear.speakers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.speakers.SpeakerRepository
import com.paligot.confily.models.ui.SpeakerUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class SpeakerUiState {
    data object Loading : SpeakerUiState()
    data class Success(val modelUi: SpeakerUi) : SpeakerUiState()
}

class SpeakerViewModel(speakerId: String, repository: SpeakerRepository) : ViewModel() {
    val uiState = repository.speaker(speakerId)
        .map { SpeakerUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = SpeakerUiState.Loading
        )
}
