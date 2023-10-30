package org.gdglille.devfest.android.theme.m3.speakers.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.gdglille.devfest.models.ui.SpeakerItemUi
import org.gdglille.devfest.repositories.SpeakerRepository

sealed class SpeakersUiState {
    data class Loading(val speakers: ImmutableList<SpeakerItemUi>) : SpeakersUiState()
    data class Success(val speakers: ImmutableList<SpeakerItemUi>) : SpeakersUiState()
    data class Failure(val throwable: Throwable) : SpeakersUiState()
}

class SpeakersListViewModel(private val repository: SpeakerRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<SpeakersUiState>(
        SpeakersUiState.Loading(
            persistentListOf(SpeakerItemUi.fake.copy(id = "1"), SpeakerItemUi.fake.copy(id = "2"))
        )
    )
    val uiState: StateFlow<SpeakersUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                repository.speakers().collect {
                    _uiState.value = SpeakersUiState.Success(it)
                }
            } catch (error: Throwable) {
                Firebase.crashlytics.recordException(error)
                _uiState.value = SpeakersUiState.Failure(error)
            }
        }
    }

    object Factory {
        fun create(repository: SpeakerRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                SpeakersListViewModel(repository = repository) as T
        }
    }
}
