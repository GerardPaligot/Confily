package org.gdglille.devfest.android.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.gdglille.devfest.repositories.AgendaRepository

class HomeViewModel(private val agendaRepository: AgendaRepository) : ViewModel() {
    fun toggleFavoriteFiltering() = viewModelScope.launch {
        agendaRepository.toggleFavoriteFiltering()
    }

    object Factory {
        fun create(agendaRepository: AgendaRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                HomeViewModel(agendaRepository = agendaRepository) as T
        }
    }
}