package com.paligot.confily.web.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.agenda.AgendaRepository
import kotlinx.coroutines.launch

class MainViewModel(private val agendaRepository: AgendaRepository): ViewModel() {
    fun loadAgenda() = viewModelScope.launch {
        agendaRepository.fetchAndStoreAgenda()
    }
}
