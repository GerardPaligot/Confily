package com.paligot.confily.backend.formats.application

import com.paligot.confily.backend.formats.domain.FormatRepository
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatEntity
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatsTable
import com.paligot.confily.backend.formats.infrastructure.exposed.toModel
import com.paligot.confily.models.Format
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class FormatRepositoryExposed(private val database: Database) : FormatRepository {
    override suspend fun list(eventId: String): List<Format> = withContext(Dispatchers.IO) {
        transaction(db = database) {
            FormatEntity
                .findByEvent(UUID.fromString(eventId))
                .orderBy(FormatsTable.createdAt to SortOrder.ASC)
                .map { it.toModel() }
        }
    }
}
