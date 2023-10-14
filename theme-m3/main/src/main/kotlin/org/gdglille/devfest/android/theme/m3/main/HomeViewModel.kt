package org.gdglille.devfest.android.theme.m3.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gdglille.devfest.android.theme.m3.main.mappers.convertToModelUi
import org.gdglille.devfest.android.theme.m3.style.actions.ScreenUi
import org.gdglille.devfest.models.ui.ExportNetworkingUi
import org.gdglille.devfest.models.ui.ScaffoldConfigUi
import org.gdglille.devfest.models.ui.UserNetworkingUi
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.EventRepository
import org.gdglille.devfest.repositories.UserRepository

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val screenUi: ScreenUi) : HomeUiState()
    data class Failure(val throwable: Throwable) : HomeUiState()
}

class HomeViewModel(
    private val agendaRepository: AgendaRepository,
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _routeState = MutableStateFlow<String?>(null)
    private val _innerRouteState = MutableStateFlow<String?>(null)
    private val _configState = agendaRepository.scaffoldConfig()
        .stateIn(
            scope = viewModelScope,
            initialValue = ScaffoldConfigUi(),
            started = SharingStarted.WhileSubscribed()
        )
    private val _uiHasFiltersState = agendaRepository.hasFilterApplied()
        .stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = SharingStarted.WhileSubscribed()
        )
    private val _exportPath = MutableSharedFlow<ExportNetworkingUi>(replay = 1)
    private val _uiState = combine(
        _routeState,
        _innerRouteState,
        _configState,
        _uiHasFiltersState,
        transform = { route, innerRoute, config, hasFilters ->
            if (route == null) return@combine HomeUiState.Loading
            return@combine HomeUiState.Success(convertToModelUi(route, innerRoute, config, hasFilters))
        }
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(), initialValue = HomeUiState.Loading)
    val exportPath: SharedFlow<ExportNetworkingUi> = _exportPath
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                agendaRepository.fetchAndStoreAgenda()
            } catch (ex: Throwable) {
                Firebase.crashlytics.recordException(ex)
            }
        }
    }

    fun screenConfig(route: String) = viewModelScope.launch {
        if (_routeState.value != route) {
            _innerRouteState.value = null
            _routeState.value = route
        }
    }

    fun innerScreenConfig(route: String) = viewModelScope.launch {
        _innerRouteState.value = route
    }

    fun saveTicket(barcode: String) = viewModelScope.launch {
        agendaRepository.insertOrUpdateTicket(barcode)
    }

    fun saveNetworkingProfile(user: UserNetworkingUi) = viewModelScope.launch {
        userRepository.insertNetworkingProfile(user)
    }

    fun disconnect() = viewModelScope.launch {
        eventRepository.deleteEventId()
    }

    fun exportNetworking() = viewModelScope.launch {
        _exportPath.tryEmit(userRepository.exportNetworking())
    }

    object Factory {
        fun create(
            agendaRepository: AgendaRepository,
            userRepository: UserRepository,
            eventRepository: EventRepository
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                HomeViewModel(
                    agendaRepository = agendaRepository,
                    userRepository = userRepository,
                    eventRepository = eventRepository
                ) as T
        }
    }
}
