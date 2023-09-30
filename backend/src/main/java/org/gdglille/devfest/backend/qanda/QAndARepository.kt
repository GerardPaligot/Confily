package org.gdglille.devfest.backend.qanda

import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.models.inputs.QAndAInput

class QAndARepository(
    private val eventDao: EventDao,
    private val qAndADao: QAndADao
) {
    suspend fun list(eventId: String, language: String) = coroutineScope {
        return@coroutineScope qAndADao.getAll(eventId, language)
            .map { it.convertToModel() }
            .sortedBy { it.order }
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
