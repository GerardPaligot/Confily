package com.paligot.confily.backend.third.parties.billetweb

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import kotlinx.coroutines.coroutineScope

class BilletWebRepository(
    private val api: BilletWebApi,
    private val eventDao: EventFirestore
) {
    suspend fun get(eventId: String, barcode: String) = coroutineScope {
        val event = eventDao.get(eventId)
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
