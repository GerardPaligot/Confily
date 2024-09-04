package org.gdglille.devfest.android.theme.m3.speakers.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.paligot.confily.core.repositories.SpeakerRepository
import com.paligot.confily.models.ui.SpeakerItemUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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

class SpeakersListViewModel(repository: SpeakerRepository) : ViewModel() {
    val uiState: StateFlow<SpeakersUiState> = repository.speakers()
        .map { SpeakersUiState.Success(it) }
        .catch {
            Firebase.crashlytics.recordException(it)
            SpeakersUiState.Failure(it)
        }
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
