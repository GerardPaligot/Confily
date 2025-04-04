package com.paligot.confily.wear.speakers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.core.speakers.SpeakerRepository
import com.paligot.confily.core.speakers.entities.mapToSpeakerUi
import com.paligot.confily.resources.Strings
import com.paligot.confily.speakers.panes.models.SpeakerUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class SpeakerUiState {
    data object Loading : SpeakerUiState()
    data class Success(val modelUi: SpeakerUi) : SpeakerUiState()
}

class SpeakerViewModel(
    speakerId: String,
    repository: SpeakerRepository,
    lyricist: Lyricist<Strings>
) : ViewModel() {
    val uiState = repository.speaker(speakerId)
        .map { SpeakerUiState.Success(it.mapToSpeakerUi(lyricist.strings)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = SpeakerUiState.Loading
        )
}
