package com.paligot.confily.backend.formats

import com.paligot.confily.models.inputs.FormatInput
import kotlinx.coroutines.coroutineScope

class FormatRepository(
    private val formatDao: FormatDao
) {
    suspend fun list(eventId: String) = coroutineScope {
        return@coroutineScope formatDao.getAll(eventId)
            .map { it.convertToModel() }
    }

    suspend fun create(eventId: String, format: FormatInput) = coroutineScope {
        formatDao.createOrUpdate(eventId, format.convertToDb())
        return@coroutineScope eventId
    }

    suspend fun update(eventId: String, formatId: String, input: FormatInput) = coroutineScope {
        formatDao.createOrUpdate(eventId, input.convertToDb(formatId))
        return@coroutineScope eventId
    }
}
