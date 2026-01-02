package com.paligot.confily.backend.sessions.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.addresses.infrastructure.exposed.toEntity
import com.paligot.confily.backend.addresses.infrastructure.provider.GeocodeApi
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatEntity
import com.paligot.confily.backend.sessions.domain.SessionAdminRepository
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionCategoriesTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionSpeakersTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionTagsTable
import com.paligot.confily.models.inputs.EventSessionInput
import com.paligot.confily.models.inputs.TalkSessionInput
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class SessionAdminRepositoryExposed(
    private val database: Database,
    private val geocodeApi: GeocodeApi
) : SessionAdminRepository {

    override suspend fun create(eventId: String, input: TalkSessionInput): String = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        val event = EventEntity[eventUuid]
        val format = FormatEntity[UUID.fromString(input.format)]

        // Create the session
        val session = SessionEntity.new {
            this.event = event
            this.format = format
            this.title = input.title
            this.description = input.abstract
            this.language = input.language
            this.level = input.level
            this.linkSlides = input.linkSlides
            this.linkReplay = input.linkReplay
        }

        val sessionId = session.id.value

        // Create speaker relationships
        if (input.speakerIds.isNotEmpty()) {
            SessionSpeakersTable.batchInsert(input.speakerIds) { speakerId ->
                this[SessionSpeakersTable.sessionId] = sessionId
                this[SessionSpeakersTable.speakerId] = UUID.fromString(speakerId)
            }
        }

        // Create category relationship
        if (input.category.isNotBlank()) {
            SessionCategoriesTable.batchInsert(listOf(input.category)) { categoryId ->
                this[SessionCategoriesTable.sessionId] = sessionId
                this[SessionCategoriesTable.categoryId] = UUID.fromString(categoryId)
            }
        }

        // Create tag relationships
        if (input.tags.isNotEmpty()) {
            SessionTagsTable.batchInsert(input.tags) { tagId ->
                this[SessionTagsTable.sessionId] = sessionId
                this[SessionTagsTable.tagId] = UUID.fromString(tagId)
            }
        }

        sessionId.toString()
    }

    override suspend fun update(eventId: String, talkId: String, input: TalkSessionInput): String =
        transaction(db = database) {
            val eventUuid = UUID.fromString(eventId)
            val uuid = UUID.fromString(talkId)
            val session = SessionEntity
                .findById(eventUuid, uuid)
                ?: throw NotFoundException("Session not found: $talkId")

            val format = FormatEntity[UUID.fromString(input.format)]

            // Update session fields
            session.format = format
            session.title = input.title
            session.description = input.abstract
            session.language = input.language
            session.level = input.level
            session.linkSlides = input.linkSlides
            session.linkReplay = input.linkReplay

            // Update speaker relationships
            SessionSpeakersTable.deleteWhere { sessionId eq uuid }
            if (input.speakerIds.isNotEmpty()) {
                SessionSpeakersTable.batchInsert(input.speakerIds) { speakerId ->
                    this[SessionSpeakersTable.sessionId] = uuid
                    this[SessionSpeakersTable.speakerId] = UUID.fromString(speakerId)
                }
            }

            // Update category relationships
            SessionCategoriesTable.deleteWhere { sessionId eq uuid }
            if (input.category.isNotBlank()) {
                SessionCategoriesTable.batchInsert(listOf(input.category)) { categoryId ->
                    this[SessionCategoriesTable.sessionId] = uuid
                    this[SessionCategoriesTable.categoryId] = UUID.fromString(categoryId)
                }
            }

            // Update tag relationships
            SessionTagsTable.deleteWhere { sessionId eq uuid }
            if (input.tags.isNotEmpty()) {
                SessionTagsTable.batchInsert(input.tags) { tagId ->
                    this[SessionTagsTable.sessionId] = uuid
                    this[SessionTagsTable.tagId] = UUID.fromString(tagId)
                }
            }

            session.id.value.toString()
        }

    override suspend fun update(eventId: String, sessionId: String, input: EventSessionInput): String {
        val address = input.address?.let {
            geocodeApi.geocode(it)
        }
        return transaction(db = database) {
            val eventUuid = UUID.fromString(eventId)
            val uuid = UUID.fromString(sessionId)
            val eventSession = EventSessionEntity
                .findById(eventUuid, uuid)
                ?: throw NotFoundException("Event session not found: $sessionId")

            eventSession.title = input.title ?: ""
            eventSession.description = input.description ?: ""

            if (address != null) {
                eventSession.address = address.toEntity()
            }

            eventSession.id.value.toString()
        }
    }
}
