package com.paligot.confily.backend.qanda

import com.paligot.confily.backend.NotFoundException
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
            val event = eventDao.get(eventId) ?: return@coroutineScope emptyList()
            return@coroutineScope qAndADao.getAll(eventId, event.defaultLanguage)
                .map { it.convertToModel() }
                .sortedBy { it.order }
        }
        return@coroutineScope qanda
    }

    suspend fun get(eventId: String, qandaId: String) = coroutineScope {
        return@coroutineScope qAndADao.get(eventId, qandaId)?.convertToModel()
            ?: throw NotFoundException("QAndA $qandaId Not Found")
    }

    suspend fun create(eventId: String, apiKey: String, qAndA: QAndAInput) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        qAndADao.createOrUpdate(eventId, qAndA.convertToDb())
        eventDao.updateUpdatedAt(event)
        return@coroutineScope eventId
    }

    suspend fun update(eventId: String, apiKey: String, qandaId: String, qAndA: QAndAInput) =
        coroutineScope {
            val event = eventDao.getVerified(eventId, apiKey)
            qAndADao.createOrUpdate(eventId, qAndA.convertToDb(qandaId))
            eventDao.updateUpdatedAt(event)
            return@coroutineScope eventId
        }
}
