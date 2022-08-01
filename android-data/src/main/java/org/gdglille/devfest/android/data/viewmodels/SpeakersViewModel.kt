package org.gdglille.devfest.android.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.gdglille.devfest.models.SpeakerItemUi
import org.gdglille.devfest.repositories.SpeakerRepository

sealed class SpeakersUiState {
    data class Loading(val speakers: List<List<SpeakerItemUi>>) : SpeakersUiState()
    data class Success(val speakers: List<List<SpeakerItemUi>>) : SpeakersUiState()
    data class Failure(val throwable: Throwable) : SpeakersUiState()
}

class SpeakersViewModel(private val repository: SpeakerRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<SpeakersUiState>(
        SpeakersUiState.Loading(
            arrayListOf(
                arrayListOf(SpeakerItemUi.fake, SpeakerItemUi.fake),
                arrayListOf(SpeakerItemUi.fake, SpeakerItemUi.fake)
            )
        )
    )
    val uiState: StateFlow<SpeakersUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                repository.speakers().collect {
                    _uiState.value = SpeakersUiState.Success(it.chunked(2))
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
                SpeakersViewModel(repository = repository) as T
        }
    }
}
