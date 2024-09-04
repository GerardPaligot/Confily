package org.gdglille.devfest.backend.formats

import com.paligot.confily.models.inputs.FormatInput
import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.events.EventDao

class FormatRepository(
    private val eventDao: EventDao,
    private val formatDao: FormatDao
) {
    suspend fun list(eventId: String) = coroutineScope {
        return@coroutineScope formatDao.getAll(eventId)
            .map { it.convertToModel() }
    }

    suspend fun get(eventId: String, formatId: String) = coroutineScope {
        return@coroutineScope formatDao.get(eventId, formatId)?.convertToModel()
            ?: throw NotFoundException("Format $formatId Not Found")
    }

    suspend fun create(eventId: String, apiKey: String, format: FormatInput) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        formatDao.createOrUpdate(eventId, format.convertToDb())
        eventDao.updateUpdatedAt(event)
        return@coroutineScope eventId
    }

    suspend fun update(
        eventId: String,
        apiKey: String,
        formatId: String,
        formatInput: FormatInput
    ) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        formatDao.createOrUpdate(eventId, formatInput.convertToDb(formatId))
        eventDao.updateUpdatedAt(event)
        return@coroutineScope eventId
    }
}
