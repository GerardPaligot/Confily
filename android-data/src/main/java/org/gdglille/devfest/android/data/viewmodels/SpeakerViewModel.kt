package org.gdglille.devfest.android.data.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.gdglille.devfest.android.data.AlarmScheduler
import org.gdglille.devfest.models.SpeakerUi
import org.gdglille.devfest.models.TalkItemUi
import org.gdglille.devfest.repositories.AgendaRepository

sealed class SpeakerUiState {
    data class Loading(val speaker: SpeakerUi) : SpeakerUiState()
    data class Success(val speaker: SpeakerUi) : SpeakerUiState()
    data class Failure(val throwable: Throwable) : SpeakerUiState()
}

class SpeakerViewModel(
    private val speakerId: String,
    private val repository: AgendaRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    private val _uiState = MutableStateFlow<SpeakerUiState>(SpeakerUiState.Loading(SpeakerUi.fake))
    val uiState: StateFlow<SpeakerUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                repository.speaker(speakerId).collect {
                    _uiState.value = SpeakerUiState.Success(it)
                }
            } catch (error: Throwable) {
                Firebase.crashlytics.recordException(error)
                _uiState.value = SpeakerUiState.Failure(error)
            }
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun markAsFavorite(context: Context, talkItem: TalkItemUi) = viewModelScope.launch {
        alarmScheduler.schedule(context, talkItem)
    }

    object Factory {
        fun create(speakerId: String, repository: AgendaRepository, alarmScheduler: AlarmScheduler) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return SpeakerViewModel(speakerId, repository, alarmScheduler) as T
                }
            }
    }
}
