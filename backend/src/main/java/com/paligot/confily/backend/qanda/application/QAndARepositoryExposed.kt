package com.paligot.confily.backend.qanda.application

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.qanda.domain.QAndARepository
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAAcronymEntity
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAActionEntity
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAActionsTable
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAEntity
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndATable
import com.paligot.confily.backend.qanda.infrastructure.exposed.toModel
import com.paligot.confily.models.QuestionAndResponse
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class QAndARepositoryExposed(private val database: Database) : QAndARepository {

    override suspend fun list(eventId: String, language: String): List<QuestionAndResponse> =
        transaction(db = database) {
            val eventUuid = UUID.fromString(eventId)
            val qanda = getByLanguage(eventUuid, language)
            if (qanda.isNotEmpty()) {
                return@transaction qanda
            }
            val event = EventEntity[eventUuid]
            return@transaction getByLanguage(eventUuid, event.defaultLanguage)
        }

    private fun getByLanguage(eventUuid: UUID, language: String): List<QuestionAndResponse> = QAndAEntity
        .findByLanguage(eventUuid, language)
        .orderBy(QAndATable.displayOrder to SortOrder.ASC)
        .map { qandaEntity ->
            val actions = QAndAActionEntity
                .findByQAndAId(qandaEntity.id.value)
                .orderBy(QAndAActionsTable.displayOrder to SortOrder.ASC)
                .map { it.toModel() }
            val acronyms = QAndAAcronymEntity
                .findByQAndAId(qandaEntity.id.value)
                .map { it.toModel() }
            qandaEntity.toModel(actions, acronyms)
        }
}
