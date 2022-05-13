package org.gdglille.devfest.android.screens.scanner.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.gdglille.devfest.repositories.AgendaRepository

class TicketQrCodeScannerViewModel(private val agendaRepository: AgendaRepository) : ViewModel() {
    fun saveTicket(barcode: String) = viewModelScope.launch {
        agendaRepository.insertOrUpdateTicket(barcode)
    }

    object Factory {
        fun create(agendaRepository: AgendaRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                TicketQrCodeScannerViewModel(agendaRepository = agendaRepository) as T
        }
    }
}