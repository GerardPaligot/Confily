package com.paligot.confily.backend.qanda.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.qanda.domain.QAndAAdminRepository
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAAcronymEntity
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAAcronymsTable
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAActionEntity
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAActionsTable
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAEntity
import com.paligot.confily.models.inputs.QAndAInput
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class QAndAAdminRepositoryExposed(private val database: Database) : QAndAAdminRepository {
    override suspend fun create(eventId: String, qAndA: QAndAInput): String = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)

        // Create Q&A
        val qandaEntity = QAndAEntity.new {
            this.event = EventEntity[eventUuid]
            this.displayOrder = qAndA.order
            this.language = qAndA.language
            this.question = qAndA.question
            this.response = qAndA.response
        }

        val qandaId = qandaEntity.id.value

        // Create actions
        qAndA.actions.forEachIndexed { index, actionInput ->
            QAndAActionEntity.new {
                this.qandaId = qandaEntity.id
                this.displayOrder = index
                this.label = actionInput.label
                this.url = actionInput.url
            }
        }

        // Create acronyms
        qAndA.acronyms.forEach { acronymInput ->
            QAndAAcronymEntity.new {
                this.qandaId = qandaEntity.id
                this.key = acronymInput.key
                this.value = acronymInput.value
            }
        }

        qandaId.toString()
    }

    override suspend fun update(eventId: String, qandaId: String, input: QAndAInput): String =
        transaction(db = database) {
            val eventUuid = UUID.fromString(eventId)
            val qandaUuid = UUID.fromString(qandaId)
            val qandaEntity = QAndAEntity
                .findById(eventUuid, qandaUuid)
                ?: throw NotFoundException("Q&A not found: $qandaId")

            // Update Q&A fields
            qandaEntity.displayOrder = input.order
            qandaEntity.language = input.language
            qandaEntity.question = input.question
            qandaEntity.response = input.response

            // Delete old actions and acronyms
            QAndAActionsTable.deleteWhere { QAndAActionsTable.qandaId eq qandaUuid }
            QAndAAcronymsTable.deleteWhere { QAndAAcronymsTable.qandaId eq qandaUuid }

            // Create new actions
            input.actions.forEachIndexed { index, actionInput ->
                QAndAActionEntity.new {
                    this.qandaId = qandaEntity.id
                    this.displayOrder = index
                    this.label = actionInput.label
                    this.url = actionInput.url
                }
            }

            // Create new acronyms
            input.acronyms.forEach { acronymInput ->
                QAndAAcronymEntity.new {
                    this.qandaId = qandaEntity.id
                    this.key = acronymInput.key
                    this.value = acronymInput.value
                }
            }

            qandaEntity.id.value.toString()
        }
}
