package com.paligot.confily.backend.speakers.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.and
import java.util.UUID

class SpeakerEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SpeakerEntity>(SpeakersTable) {
        fun findByEvent(eventId: UUID): SizedIterable<SpeakerEntity> = this
            .find { (SpeakersTable.eventId eq eventId) }

        fun findByIdsWithEmail(eventId: UUID, speakerIds: List<UUID>): SizedIterable<SpeakerEntity> = this
            .find {
                val eventOp = SpeakersTable.eventId eq eventId
                val speakerIdsOp = SpeakersTable.id inList speakerIds
                val emailOp = SpeakersTable.email neq null
                eventOp and speakerIdsOp and emailOp
            }

        fun findById(eventId: UUID, speakerId: UUID): SpeakerEntity? = this
            .find { (SpeakersTable.eventId eq eventId) and (SpeakersTable.id eq speakerId) }
            .firstOrNull()

        fun findByExternalId(eventId: UUID, externalId: String): SpeakerEntity? = this
            .find { (SpeakersTable.eventId eq eventId) and (SpeakersTable.externalId eq externalId) }
            .firstOrNull()

        fun findByExternalIds(eventId: UUID, speakerIds: List<String>): List<SpeakerEntity> = this
            .find { (SpeakersTable.eventId eq eventId) and (SpeakersTable.externalId inList speakerIds) }
            .toList()
    }

    var eventId by SpeakersTable.eventId
    var event by EventEntity.Companion referencedOn SpeakersTable.eventId
    var name by SpeakersTable.name
    var bio by SpeakersTable.bio
    var photoUrl by SpeakersTable.photoUrl
    var pronouns by SpeakersTable.pronouns
    var email by SpeakersTable.email
    var company by SpeakersTable.company
    var jobTitle by SpeakersTable.jobTitle
    var externalId by SpeakersTable.externalId
    var createdAt by SpeakersTable.createdAt
    var updatedAt by SpeakersTable.updatedAt
}
