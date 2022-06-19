package org.gdglille.devfest.android.screens.partners

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.gdglille.devfest.models.PartnerGroupsUi
import org.gdglille.devfest.repositories.AgendaRepository

sealed class PartnerUiState {
    data class Loading(val partners: PartnerGroupsUi) : PartnerUiState()
    data class Success(val partners: PartnerGroupsUi) : PartnerUiState()
    data class Failure(val throwable: Throwable) : PartnerUiState()
}

class PartnersViewModel(private val repository: AgendaRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<PartnerUiState>(PartnerUiState.Loading(PartnerGroupsUi.fake))
    val uiState: StateFlow<PartnerUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                repository.partners().collect {
                    _uiState.value = PartnerUiState.Success(it)
                }
            } catch (error: Throwable) {
                Firebase.crashlytics.recordException(error)
                _uiState.value = PartnerUiState.Failure(error)
            }
        }
    }

    object Factory {
        fun create(repository: AgendaRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                PartnersViewModel(repository = repository) as T
        }
    }
}