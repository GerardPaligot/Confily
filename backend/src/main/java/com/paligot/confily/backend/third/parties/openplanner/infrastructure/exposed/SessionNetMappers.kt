package com.paligot.confily.backend.third.parties.openplanner.infrastructure.exposed

import com.paligot.confily.backend.categories.infrastructure.exposed.CategoryEntity
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatEntity
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionCategoriesTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionSpeakersTable
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerEntity
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.SessionOP
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert

fun SessionOP.toEventSessionEntity(event: EventEntity): EventSessionEntity = EventSessionEntity
    .findByExternalId(eventId = event.id.value, externalId = this.id, provider = IntegrationProvider.OPENPLANNER)
    ?.let { entity ->
        entity.title = this@toEventSessionEntity.title
        entity.description = this@toEventSessionEntity.abstract
        entity.externalId = this@toEventSessionEntity.id
        entity
    }
    ?: EventSessionEntity.new {
        this.event = event
        this.title = this@toEventSessionEntity.title
        this.description = this@toEventSessionEntity.abstract
        this.externalId = this@toEventSessionEntity.id
    }

fun SessionOP.toSessionEntity(event: EventEntity): SessionEntity {
    val session = SessionEntity
        .findByExternalId(eventId = event.id.value, externalId = this.id, provider = IntegrationProvider.OPENPLANNER)
        ?.let { entity ->
            entity.format = this@toSessionEntity.formatId?.let { formatId ->
                FormatEntity.findByExternalId(
                    eventId = event.id.value,
                    externalId = formatId,
                    provider = IntegrationProvider.OPENPLANNER
                )
            }
            entity.title = this@toSessionEntity.title
            entity.description = this@toSessionEntity.abstract
            entity.language = this@toSessionEntity.language ?: event.defaultLanguage
            entity.level = this@toSessionEntity.level
            entity.externalId = this@toSessionEntity.id
            entity
        }
        ?: SessionEntity.new {
            this.event = event
            this.format = this@toSessionEntity.formatId?.let { formatId ->
                FormatEntity.findByExternalId(
                    eventId = event.id.value,
                    externalId = formatId,
                    provider = IntegrationProvider.OPENPLANNER
                )
            }
            this.title = this@toSessionEntity.title
            this.description = this@toSessionEntity.abstract
            this.language = this@toSessionEntity.language ?: event.defaultLanguage
            this.level = this@toSessionEntity.level
            this.externalId = this@toSessionEntity.id
            this.externalProvider = IntegrationProvider.OPENPLANNER
        }
    SessionSpeakersTable.deleteWhere { SessionSpeakersTable.sessionId eq session.id.value }
    SpeakerEntity
        .findByExternalIds(
            eventId = event.id.value,
            speakerIds = this.speakerIds,
            provider = IntegrationProvider.OPENPLANNER
        )
        .forEach { speaker ->
            SessionSpeakersTable.insert {
                it[this.sessionId] = session.id.value
                it[this.speakerId] = speaker.id.value
            }
        }
    if (this.categoryId != null) {
        SessionCategoriesTable.deleteWhere { SessionCategoriesTable.sessionId eq session.id.value }
        CategoryEntity
            .findByExternalId(
                eventId = event.id.value,
                externalId = this.categoryId,
                provider = IntegrationProvider.OPENPLANNER
            )
            ?.let { category ->
                SessionCategoriesTable.insert {
                    it[this.sessionId] = session.id.value
                    it[this.categoryId] = category.id.value
                }
            }
    }
    return session
}
