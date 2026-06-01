package com.paligot.confily.backend.quiz.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class QuizSubmissionEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<QuizSubmissionEntity>(QuizSubmissionsTable) {
        fun findByEvent(eventId: UUID): SizedIterable<QuizSubmissionEntity> = this
            .find { QuizSubmissionsTable.eventId eq eventId }

        fun findByPlayerAndPartner(playerId: UUID, partnerId: UUID): QuizSubmissionEntity? = this.find {
            (QuizSubmissionsTable.playerId eq playerId) and (QuizSubmissionsTable.partnerId eq partnerId)
        }.firstOrNull()
    }

    var eventId by QuizSubmissionsTable.eventId
    var event by EventEntity.Companion referencedOn QuizSubmissionsTable.eventId
    var playerId by QuizSubmissionsTable.playerId
    var player by QuizPlayerEntity referencedOn QuizSubmissionsTable.playerId
    var partnerId by QuizSubmissionsTable.partnerId
    var partner by PartnerEntity.Companion referencedOn QuizSubmissionsTable.partnerId
    var correctCount by QuizSubmissionsTable.correctCount
    var totalCount by QuizSubmissionsTable.totalCount
    var submittedAt by QuizSubmissionsTable.submittedAt
}
