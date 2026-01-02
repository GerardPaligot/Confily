package com.paligot.confily.backend.speakers.application

import com.paligot.confily.backend.speakers.domain.SpeakerRepository
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerEntity
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakersTable
import com.paligot.confily.backend.speakers.infrastructure.exposed.toModel
import com.paligot.confily.models.Speaker
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class SpeakerRepositoryExposed(private val database: Database) : SpeakerRepository {
    override suspend fun list(eventId: String): List<Speaker> = transaction(db = database) {
        val uuid = UUID.fromString(eventId)
        SpeakerEntity.find { SpeakersTable.eventId eq uuid }
            .orderBy(SpeakersTable.name to SortOrder.ASC)
            .map { it.toModel() }
    }
}
