package org.gdglille.devfest.backend.billetweb

import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.NotAcceptableException
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.internals.network.billetweb.BilletWebApi

class BilletWebRepository(
    private val api: BilletWebApi,
    private val eventDao: EventDao
) {
    suspend fun get(eventId: String, barcode: String) = coroutineScope {
        val event = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        if (event.billetWebConfig == null) throw NotAcceptableException("BilletWeb config not initialized")
        val attendees = api.fetchAttendee(
            event.billetWebConfig.eventId,
            event.billetWebConfig.userId,
            event.billetWebConfig.apiKey,
            barcode
        )
        if (attendees.isEmpty()) throw NotFoundException("$barcode Not Found")
        return@coroutineScope attendees.first()
    }
}
