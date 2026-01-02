package com.paligot.confily.backend.export.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.events.infrastructure.storage.EventStorage
import com.paligot.confily.backend.export.domain.ExportRepository
import com.paligot.confily.models.PartnersActivities

class ExportPartnersRepositoryDefault(
    private val eventFirestore: EventFirestore,
    private val eventStorage: EventStorage
) : ExportRepository<PartnersActivities> {
    override suspend fun get(eventId: String): PartnersActivities {
        val eventDb = eventFirestore.get(eventId)
        return eventStorage.getPartnersFile(eventId, eventDb.partnersUpdatedAt)
            ?: throw NotFoundException("Partners $eventId Not Found")
    }
}
