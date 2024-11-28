package com.paligot.confily.wear.speakers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.speakers.SpeakerRepository
import com.paligot.confily.core.speakers.entities.SpeakerItem
import com.paligot.confily.wear.speakers.panes.SpeakerModelUi
import com.paligot.confily.wear.speakers.panes.SpeakersModelUi
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class SpeakersUiState {
    data object Loading : SpeakersUiState()
    data class Success(val modelUi: SpeakersModelUi) : SpeakersUiState()
}

class SpeakersViewModel(repository: SpeakerRepository) : ViewModel() {
    val uiState = repository.speakers()
        .map { SpeakersModelUi(it.map { it.toModelUi() }.toImmutableList()) }
        .map { SpeakersUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = SpeakersUiState.Loading
        )
}

fun SpeakerItem.toModelUi() = SpeakerModelUi(
    id = id,
    name = displayName,
    url = photoUrl,
    job = jobTitle ?: company ?: ""
)
