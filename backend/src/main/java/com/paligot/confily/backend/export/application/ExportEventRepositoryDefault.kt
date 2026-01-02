package com.paligot.confily.backend.export.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.events.infrastructure.storage.EventStorage
import com.paligot.confily.backend.export.domain.ExportRepository
import com.paligot.confily.models.ExportEvent

class ExportEventRepositoryDefault(
    private val eventDao: EventFirestore,
    private val eventStorage: EventStorage
) : ExportRepository<ExportEvent> {
    override suspend fun get(eventId: String): ExportEvent {
        val eventDb = eventDao.get(eventId)
        return eventStorage.getEventFile(eventId, eventDb.eventUpdatedAt)
            ?: throw NotFoundException("Event $eventId Not Found")
    }
}
