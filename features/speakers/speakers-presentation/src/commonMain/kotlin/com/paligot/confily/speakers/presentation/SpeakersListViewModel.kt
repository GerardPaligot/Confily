package com.paligot.confily.speakers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.core.speakers.SpeakerRepository
import com.paligot.confily.core.speakers.entities.mapToSpeakerItemUi
import com.paligot.confily.models.ui.SpeakerItemUi
import com.paligot.confily.resources.Strings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class SpeakersUiState {
    data class Loading(val speakers: ImmutableList<SpeakerItemUi>) : SpeakersUiState()
    data class Success(val speakers: ImmutableList<SpeakerItemUi>) : SpeakersUiState()
    data class Failure(val throwable: Throwable) : SpeakersUiState()
}

class SpeakersListViewModel(
    repository: SpeakerRepository,
    lyricist: Lyricist<Strings>
) : ViewModel() {
    val uiState: StateFlow<SpeakersUiState> = repository.speakers()
        .map { SpeakersUiState.Success(it.map { it.mapToSpeakerItemUi(lyricist.strings) }.toImmutableList()) }
        .catch { SpeakersUiState.Failure(it) }
        .stateIn(
            scope = viewModelScope,
            initialValue = SpeakersUiState.Loading(
                persistentListOf(
                    SpeakerItemUi.fake.copy(id = "1"),
                    SpeakerItemUi.fake.copy(id = "2")
                )
            ),
            started = SharingStarted.WhileSubscribed()
        )
}
