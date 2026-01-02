package com.paligot.confily.backend.formats.application

import com.paligot.confily.backend.formats.domain.FormatRepository
import com.paligot.confily.backend.formats.infrastructure.firestore.FormatEntity
import com.paligot.confily.backend.formats.infrastructure.firestore.FormatFirestore
import com.paligot.confily.backend.formats.infrastructure.firestore.convertToModel
import com.paligot.confily.models.Format

class FormatRepositoryDefault(
    private val formatFirestore: FormatFirestore
) : FormatRepository {
    override suspend fun list(eventId: String): List<Format> = formatFirestore
        .getAll(eventId)
        .map(FormatEntity::convertToModel)
}
