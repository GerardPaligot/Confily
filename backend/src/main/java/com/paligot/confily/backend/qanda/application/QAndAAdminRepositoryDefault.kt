package com.paligot.confily.backend.qanda.application

import com.paligot.confily.backend.internals.infrastructure.firestore.QAndAFirestore
import com.paligot.confily.backend.qanda.domain.QAndAAdminRepository
import com.paligot.confily.models.inputs.QAndAInput
import kotlinx.coroutines.coroutineScope

class QAndAAdminRepositoryDefault(
    private val qAndAFirestore: QAndAFirestore
) : QAndAAdminRepository {
    override suspend fun create(eventId: String, qAndA: QAndAInput): String = coroutineScope {
        qAndAFirestore.createOrUpdate(eventId, qAndA.convertToEntity())
        return@coroutineScope eventId
    }

    override suspend fun update(eventId: String, qandaId: String, input: QAndAInput): String = coroutineScope {
        qAndAFirestore.createOrUpdate(eventId, input.convertToEntity(qandaId))
        return@coroutineScope eventId
    }
}
