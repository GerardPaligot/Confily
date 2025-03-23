package com.paligot.confily.backend.qanda

import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.models.inputs.QAndAInput
import kotlinx.coroutines.coroutineScope

class QAndARepository(
    private val eventDao: EventDao,
    private val qAndADao: QAndADao
) {
    suspend fun list(eventId: String, language: String) = coroutineScope {
        val qanda = qAndADao.getAll(eventId, language)
            .map { it.convertToModel() }
            .sortedBy { it.order }
        if (qanda.isEmpty()) {
            val event = eventDao.get(eventId)
            return@coroutineScope qAndADao.getAll(eventId, event.defaultLanguage)
                .map { it.convertToModel() }
                .sortedBy { it.order }
        }
        return@coroutineScope qanda
    }

    suspend fun create(eventId: String, qAndA: QAndAInput) = coroutineScope {
        qAndADao.createOrUpdate(eventId, qAndA.convertToDb())
        return@coroutineScope eventId
    }

    suspend fun update(eventId: String, qandaId: String, input: QAndAInput) = coroutineScope {
        qAndADao.createOrUpdate(eventId, input.convertToDb(qandaId))
        return@coroutineScope eventId
    }
}
