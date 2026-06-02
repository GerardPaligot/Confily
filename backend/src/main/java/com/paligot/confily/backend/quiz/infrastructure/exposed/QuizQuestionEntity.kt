package com.paligot.confily.backend.quiz.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class QuizQuestionEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<QuizQuestionEntity>(QuizQuestionsTable) {
        fun findByEvent(eventId: UUID): SizedIterable<QuizQuestionEntity> = this
            .find { QuizQuestionsTable.eventId eq eventId }

        fun findByPartner(partnerId: UUID): SizedIterable<QuizQuestionEntity> = this
            .find { QuizQuestionsTable.partnerId eq partnerId }

        fun findByExternalId(
            eventId: UUID,
            externalId: String,
            provider: IntegrationProvider
        ): QuizQuestionEntity? = this.find {
            (QuizQuestionsTable.eventId eq eventId) and
                (QuizQuestionsTable.externalId eq externalId) and
                (QuizQuestionsTable.externalProvider eq provider)
        }.firstOrNull()
    }

    var eventId by QuizQuestionsTable.eventId
    var event by EventEntity.Companion referencedOn QuizQuestionsTable.eventId
    var partnerId by QuizQuestionsTable.partnerId
    var partner by PartnerEntity.Companion referencedOn QuizQuestionsTable.partnerId
    var displayOrder by QuizQuestionsTable.displayOrder
    var question by QuizQuestionsTable.question
    var externalId by QuizQuestionsTable.externalId
    var externalProvider by QuizQuestionsTable.externalProvider
    var createdAt by QuizQuestionsTable.createdAt
    var updatedAt by QuizQuestionsTable.updatedAt
}
