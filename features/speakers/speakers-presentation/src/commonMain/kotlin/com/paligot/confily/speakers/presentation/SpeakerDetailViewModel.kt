package com.paligot.confily.speakers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.core.AlarmScheduler
import com.paligot.confily.core.speakers.SpeakerRepository
import com.paligot.confily.core.speakers.entities.mapToUi
import com.paligot.confily.models.ui.SpeakerUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.resources.Strings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class SpeakerUiState {
    data class Loading(val speaker: SpeakerUi) : SpeakerUiState()
    data class Success(val speaker: SpeakerUi) : SpeakerUiState()
    data class Failure(val throwable: Throwable) : SpeakerUiState()
}

class SpeakerDetailViewModel(
    speakerId: String,
    repository: SpeakerRepository,
    lyricist: Lyricist<Strings>,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    val uiState: StateFlow<SpeakerUiState> = repository.speaker(speakerId)
        .map { SpeakerUiState.Success(it.mapToUi(lyricist.strings)) }
        .catch { SpeakerUiState.Failure(it) }
        .stateIn(
            scope = viewModelScope,
            initialValue = SpeakerUiState.Loading(SpeakerUi.fake),
            started = SharingStarted.WhileSubscribed()
        )

    fun markAsFavorite(talkItem: TalkItemUi) = viewModelScope.launch {
        alarmScheduler.schedule(talkItem)
    }
}
