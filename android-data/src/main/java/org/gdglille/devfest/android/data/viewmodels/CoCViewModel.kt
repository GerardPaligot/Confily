package org.gdglille.devfest.android.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.gdglille.devfest.models.CoCUi
import org.gdglille.devfest.repositories.AgendaRepository

sealed class CoCUiState {
    object Loading : CoCUiState()
    data class Success(val coc: CoCUi) : CoCUiState()
    data class Failure(val throwable: Throwable) : CoCUiState()
}

class CoCViewModel(private val repository: AgendaRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<CoCUiState>(CoCUiState.Loading)
    val uiState: StateFlow<CoCUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                repository.coc().collect {
                    _uiState.value = CoCUiState.Success(it)
                }
            } catch (error: Throwable) {
                Firebase.crashlytics.recordException(error)
                _uiState.value = CoCUiState.Failure(error)
            }
        }
    }

    object Factory {
        fun create(repository: AgendaRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = CoCViewModel(repository = repository) as T
        }
    }
}
