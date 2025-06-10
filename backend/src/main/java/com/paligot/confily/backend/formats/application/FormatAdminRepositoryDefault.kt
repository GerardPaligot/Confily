package com.paligot.confily.backend.formats.application

import com.paligot.confily.backend.formats.domain.FormatAdminRepository
import com.paligot.confily.backend.internals.infrastructure.firestore.FormatFirestore
import com.paligot.confily.models.inputs.FormatInput

class FormatAdminRepositoryDefault(
    private val formatFirestore: FormatFirestore
) : FormatAdminRepository {
    override suspend fun create(eventId: String, format: FormatInput): String {
        formatFirestore.createOrUpdate(eventId, format.convertToEntity())
        return eventId
    }

    override suspend fun update(eventId: String, formatId: String, input: FormatInput): String {
        formatFirestore.createOrUpdate(eventId, input.convertToEntity(formatId))
        return eventId
    }
}
