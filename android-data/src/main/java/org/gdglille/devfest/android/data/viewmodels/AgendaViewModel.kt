package org.gdglille.devfest.android.data.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.gdglille.devfest.android.data.AlarmScheduler
import org.gdglille.devfest.models.AgendaUi
import org.gdglille.devfest.models.TalkItemUi
import org.gdglille.devfest.repositories.AgendaRepository
import java.net.UnknownHostException

sealed class AgendaUiState {
    data class Loading(val agenda: AgendaUi) : AgendaUiState()
    data class Success(val agenda: AgendaUi) : AgendaUiState()
    data class Failure(val throwable: Throwable) : AgendaUiState()
}

@FlowPreview
@ExperimentalCoroutinesApi
class AgendaViewModel(
    private val repository: AgendaRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    private val _uiState = MutableStateFlow<AgendaUiState>(AgendaUiState.Loading(AgendaUi.fake))
    val uiState: StateFlow<AgendaUiState> = _uiState

    init {
        viewModelScope.launch {
            arrayListOf(
                async {
                    try {
                        repository.fetchAndStoreAgenda()
                    } catch (ignored: UnknownHostException) {
                    } catch (error: Throwable) {
                        Firebase.crashlytics.recordException(error)
                    }
                },
                async {
                    try {
                        repository.agenda().collect {
                            _uiState.value = AgendaUiState.Success(it)
                        }
                    } catch (error: Throwable) {
                        Firebase.crashlytics.recordException(error)
                        _uiState.value = AgendaUiState.Failure(error)
                    }
                }
            ).awaitAll()
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun markAsFavorite(context: Context, talkItem: TalkItemUi) = viewModelScope.launch {
        alarmScheduler.schedule(context, talkItem)
    }

    object Factory {
        fun create(repository: AgendaRepository, alarmScheduler: AlarmScheduler) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T = AgendaViewModel(
                    repository = repository,
                    alarmScheduler = alarmScheduler
                ) as T
            }
    }
}
