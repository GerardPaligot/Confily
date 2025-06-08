package com.paligot.confily.backend.third.parties.openfeedback.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.storage.EventStorage
import com.paligot.confily.backend.third.parties.openfeedback.domain.OpenfeedbackRepository
import com.paligot.confily.models.OpenFeedback

class OpenfeedbackRepositoryDefault(
    private val eventFirestore: EventFirestore,
    private val eventStorage: EventStorage
) : OpenfeedbackRepository {
    override suspend fun get(eventId: String): OpenFeedback {
        val eventDb = eventFirestore.get(eventId)
        val planning = eventStorage.getPlanningFile(eventId, eventDb.agendaUpdatedAt)
            ?: throw NotFoundException("Event $eventId Not Found")
        return planning.convertToOpenFeedback()
    }
}
