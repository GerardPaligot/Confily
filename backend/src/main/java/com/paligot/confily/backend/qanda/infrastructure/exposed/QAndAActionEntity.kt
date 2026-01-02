package com.paligot.confily.backend.qanda.infrastructure.exposed

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import java.util.UUID

class QAndAActionEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<QAndAActionEntity>(QAndAActionsTable) {
        fun findByQAndAId(qandaId: UUID): SizedIterable<QAndAActionEntity> = this
            .find { QAndAActionsTable.qandaId eq qandaId }
    }

    var qandaId by QAndAActionsTable.qandaId
    var qanda by QAndAEntity referencedOn QAndAActionsTable.qandaId
    var displayOrder by QAndAActionsTable.displayOrder
    var label by QAndAActionsTable.label
    var url by QAndAActionsTable.url
    var createdAt by QAndAActionsTable.createdAt
}
