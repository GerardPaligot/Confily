package org.gdglille.devfest.android.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.gdglille.devfest.models.PartnerItemUi
import org.gdglille.devfest.repositories.AgendaRepository

sealed class PartnerUiState {
    data class Loading(val partner: PartnerItemUi) : PartnerUiState()
    data class Success(val partner: PartnerItemUi) : PartnerUiState()
    data class Failure(val throwable: Throwable) : PartnerUiState()
}

class PartnerViewModel(
    private val partnerId: String,
    private val repository: AgendaRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<PartnerUiState>(PartnerUiState.Loading(PartnerItemUi.fake))
    val uiState: StateFlow<PartnerUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                repository.partner(partnerId).collect {
                    _uiState.value = PartnerUiState.Success(it)
                }
            } catch (error: Throwable) {
                Firebase.crashlytics.recordException(error)
                _uiState.value = PartnerUiState.Failure(error)
            }
        }
    }

    object Factory {
        fun create(partnerId: String, repository: AgendaRepository) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return PartnerViewModel(partnerId, repository) as T
                }
            }
    }
}
