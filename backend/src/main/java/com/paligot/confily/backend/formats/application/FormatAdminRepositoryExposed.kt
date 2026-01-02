package com.paligot.confily.backend.formats.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.formats.domain.FormatAdminRepository
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatEntity
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatsTable
import com.paligot.confily.models.inputs.FormatInput
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class FormatAdminRepositoryExposed(private val database: Database) : FormatAdminRepository {
    override suspend fun create(eventId: String, format: FormatInput): String = transaction(database) {
        val eventUuid = UUID.fromString(eventId)
        val event = EventEntity[eventUuid]
        FormatEntity.new {
            name = format.name
            this.event = event
        }.id.value.toString()
    }

    override suspend fun update(eventId: String, formatId: String, input: FormatInput): String = transaction(database) {
        val eventUuid = UUID.fromString(eventId)
        val formatUuid = UUID.fromString(formatId)
        val format = FormatEntity
            .find { FormatsTable.eventId eq eventUuid and (FormatsTable.id eq formatUuid) }
            .firstOrNull() ?: throw NotFoundException("Format not found")
        format.name = input.name
        format.id.value.toString()
    }
}
